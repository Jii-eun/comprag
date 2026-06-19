package com.proj.comprag.service.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class RagService {

    public String answer(String question) {

        return "";
    }
}
