package com.pilnyck;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkServiceTest {
    @Test
    public void testCheckDeep(){
        String path = "https://www.google.com/";
        LinkService linkService = new LinkService(path);
        int actual = linkService.checkDeep(path);
        int expected = 1;
        assertEquals(expected, actual);
    }

//    @Test
//    public void testCheckDeepWhenDeepIsThree(){
//        //String path = "https://www.google.com/wiki/java";
//        String path = "https://www.google.com/";
//        LinkService linkService = new LinkService(path);
//        int actual = linkService.checkDeep(path);
//        int expected = 3;
//        assertEquals(expected, actual);
//    }
}