package com.gm.wj.dao;

import com.gm.wj.entity.UserBehavior;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBehaviorDAO extends JpaRepository<UserBehavior,Integer> {
    UserBehavior getByUidAndGid(int uid,int gid);
    List<UserBehavior>getByUid(int uid);
}
