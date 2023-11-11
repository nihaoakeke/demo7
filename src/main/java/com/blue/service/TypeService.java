package com.blue.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.dao.TypeDao;
import com.blue.domain.Type;
import org.springframework.stereotype.Service;

@Service
public class TypeService extends ServiceImpl<TypeDao, Type> {
}
