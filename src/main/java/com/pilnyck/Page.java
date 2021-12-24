package com.pilnyck;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
//@Builder
@Getter
@Setter
public class Page {
    String pathName;
    int countForeignLinks;
    int deepLevel;

    public Page() {

    }
}
