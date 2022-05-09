package com.gm.wj.service;

import com.gm.wj.dao.UserBehaviorDAO;
import com.gm.wj.entity.UserBehavior;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserBehaviorService {
    @Autowired
    UserBehaviorDAO userBehaviorDAO;

    @Autowired
    DataSource dataSource;

    public List<UserBehavior>list(){
        List<UserBehavior> userBehaviors = userBehaviorDAO.findAll();
        return userBehaviors;
    }
    public void save(UserBehavior userBehavior){
        Date date1 = new Date();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        userBehavior.setTime(formatter.format(date1));
        userBehaviorDAO.save(userBehavior);
    }
    public void update(UserBehavior userBehavior){
        UserBehavior userBehavior1 =userBehaviorDAO.getByUidAndGid(userBehavior.getUid(), userBehavior.getGid());
        Date date1 = new Date();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        userBehavior1.setTime(formatter.format(date1));
        userBehavior1.setBehavior_type(userBehavior.getBehavior_type());
        userBehaviorDAO.save(userBehavior1);
    }
    public List<Long>recommend(int uid){
//        List<UserBehavior> userBehaviors = userBehaviorDAO.findAll();
        System.out.println("uid"+uid);
        List<Long>result = new ArrayList<>();
        try{

            JDBCDataModel model = new MySQLJDBCDataModel(dataSource,"user_behavior","uid",
                    "gid","behavior_type","time");

            DataModel dataModel = model;

            UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(dataModel);
            UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(2,userSimilarity,dataModel);
            //定义推荐引擎
            Recommender recommender = new GenericUserBasedRecommender(dataModel,userNeighborhood,userSimilarity);
            LongPrimitiveIterator usersIterator = dataModel.getUserIDs();

            //userID的N-最近邻
            long[] userN = userNeighborhood.getUserNeighborhood(uid);
            System.out.println("用户 "+uid + " 的2-最近邻是 "+ Arrays.toString(userN));
            //用户userID的推荐物品，最多推荐两个
            List<RecommendedItem> recommendedItems = recommender.recommend(uid, 10);
            for (RecommendedItem item : recommendedItems) {
                System.out.println("推荐的物品"+ item.getItemID()+"预测评分是 "+ item.getValue());
                result.add(item.getItemID());
            }
            return result;

        }catch (Exception e){
            e.printStackTrace();

        }
        return null;
    }

    public void addData(int uid,int gid){
        Random rd = new Random();
        List<UserBehavior> userBehaviors = new ArrayList<>();
        for(int i=0;i<20;i++){
            for(int j=0;j<100;j++){
                UserBehavior userBehavior = new UserBehavior();
                userBehavior.setUid(i+1);
                userBehavior.setBehavior_type(rd.nextInt(6));
                userBehavior.setGid(j+1);
                userBehaviors.add(userBehavior);
            }
        }
        userBehaviorDAO.saveAll(userBehaviors);
    }
}
