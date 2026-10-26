package com.satc.edu.ordem_producao.common.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, Object id) {
        super(resource + " não encontrado: " + id);
    }

}
