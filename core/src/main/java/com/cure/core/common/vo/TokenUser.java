package com.cure.core.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @title: TokenUser
 * @description:
 * @author: dengmiao
 * @create: 2019-05-17 17:40
 **/
@Data
@AllArgsConstructor
public class TokenUser implements Serializable {

    private String username;

    private List<String> permissions;

    private Boolean saveLogin;
}
