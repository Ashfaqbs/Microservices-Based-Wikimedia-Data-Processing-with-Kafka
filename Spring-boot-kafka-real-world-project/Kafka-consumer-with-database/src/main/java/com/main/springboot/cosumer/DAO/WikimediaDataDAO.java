package com.main.springboot.cosumer.DAO;

import com.main.springboot.cosumer.entity.WikimediaData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WikimediaDataDAO extends JpaRepository<WikimediaData,Long> {
        }
