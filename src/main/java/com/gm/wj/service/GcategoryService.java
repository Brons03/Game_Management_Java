package com.gm.wj.service;

import com.gm.wj.dao.GcategoryDAO;
import com.gm.wj.entity.Gcategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GcategoryService {
    @Autowired
    GcategoryDAO categoryDAO;

    public List<Gcategory> list() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return categoryDAO.findAll(sort);
    }

    public Gcategory get(int id) {
        Gcategory c= categoryDAO.findById(id).orElse(null);
        return c;
    }
}
