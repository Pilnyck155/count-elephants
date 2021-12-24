
import com.pilnyck.*;
//import com.pilnyck.ListFactory;

import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args) {
        String path = "https://javarush.ru"; // enter from keyboard
        //String path = "https://www.google.com/"; // enter from keyboard
        //String path = "https://ubuntu.ru/"; // enter from keyboard
        //String path = "https://horstmann.com/";

        //String content = Connector.getContent(path);
        LinkService linkService = new LinkService(path);
        List<Page> pageList = linkService.engineLinkService(path);
        int foreignLinks = 0;
        for (Page page : pageList) {
            System.out.println(page);
            foreignLinks += page.getCountForeignLinks();
        }
        System.out.println("Domain: " + path + ", foreign links: " + foreignLinks + ", count of pages: " + linkService.getNativeLinksCounter());
    }
}
