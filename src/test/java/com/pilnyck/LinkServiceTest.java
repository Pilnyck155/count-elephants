package com.pilnyck;

import jdk.jfr.Name;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LinkServiceTest {
    String path = "https://www.google.com/";
    LinkService linkService = new LinkService(path);

    @Name("test check deep method")
    @Test
    public void testCheckDeep() {
        int actual = linkService.checkDeep(path);
        int expected = 0;
        assertEquals(expected, actual);
    }

//    @Name("test get all links method")
//    @Test
//    public void testGetAllLinks() {
//        String content = "<li><a href='bjlo/index.html'>Big Java Late Objects</a> | " +
//                "<a href='bjlo/index.html'>Java Concepts Late Objects</a></li> \n" +
//                "      <li><a href='bigcpp/index.html'>Big C++</a> |" +
//                " <a href='bigcpp/index.html'>Brief C++</a> (former title: C++ for Everyone)</li> \n" +
//                "      <li><a href='python4everyone/index.html'>Python for Everyone</a></li> \n" +
//                "      <li><a href='corejava/index.html'>Core Java</a></li> \n" +
//                "      <li><a href='javaimpatient/index.html'>Core Java for the Impatient</a></li> ";
//        List<String> actualLinks = linkService.getAllLinks(content);
//        List<String> expectedLinks = List.of("bjlo/index.html", "bjlo/index.html", "bigcpp/index.html", "bigcpp/index.html",
//                "python4everyone/index.html", "corejava/index.html", "javaimpatient/index.html");
//        assertEquals(expectedLinks, actualLinks);
//    }

    @Name("test get native links")
    @Test
    public void testGetNativeLinks(){
        //linkService.getNativeLinksCounter()
    }
}