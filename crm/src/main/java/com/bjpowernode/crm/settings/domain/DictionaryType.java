package com.bjpowernode.crm.settings.domain;

import com.sun.xml.internal.ws.developer.Serialization;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
@Data
@Accessors(chain = true)
public class DictionaryType implements Serializable {
    private String code;
    private String name;
    private String description;
}
