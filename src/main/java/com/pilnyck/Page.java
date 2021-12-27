package com.pilnyck;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Page {
    String pathName;
    int countForeignLinks;
    int deepLevel;
}
