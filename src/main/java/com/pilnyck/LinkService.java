package com.pilnyck;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkService {

    //TODO: ШУКАТИ ТІЛЬКИ HTMLD
    //String patternString = "href\\s*=\\s*(\"[^\"]*\"|[^\\s>]*)\\s*>";
    String patternString = "href\\s*=\\s*(\"[^\"]*\"|[^\\s>]*)\\s*html";
    Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);

    String mainPath;

    public LinkService(String mainPath) {
        this.mainPath = mainPath;
    }

    public int getNativeLinksCounter() {
        return nativeLinksCounter;
    }

    List<String> nativeLinks = new ArrayList<>();
    //List<String> foreignLinks = new ArrayList<>();
    //List<String> allLinks = new ArrayList<>();
    int foreignLinksCounter;
    int nativeLinksCounter;
    List<Page> listOfPages = new ArrayList<>();


    public List<Page> engineLinkService(String currentPath) {
        if (currentPath == null) {
            currentPath = mainPath;
        }

        Page pageEntity = new Page();
        pageEntity.setPathName(currentPath);
        listOfPages.add(pageEntity);

        String content = Connector.getContent(currentPath);
        List<String> allLinks = getAllLinks(content);
        nativeLinks = getNativeLinks(allLinks, currentPath);

        int deep = checkDeep(currentPath);

        pageEntity.setDeepLevel(deep);
        pageEntity.setCountForeignLinks(foreignLinksCounter);

        nativeLinksCounter = nativeLinks.size();
        return listOfPages;
    }

    public List<String> getAllLinks(String content) {
        List<String> links = new ArrayList<>();
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String match = content.substring(start, end);
            String updateStartLink = match.replace("href=", "");

            String updateEndLink = updateStartLink.replace("\"/>", "");
            String updateWithApostropheLink = updateEndLink.replace("'", "");
            String updateLink = updateWithApostropheLink.replace("\"", "");
            String updateWithDotsLink = updateLink.replace("../", "");
            links.add(updateWithDotsLink);
        }
        return links;
    }

    public List<String> getNativeLinks(List<String> allLinks, String currentPath) {
        Pattern mainPathPattern = Pattern.compile(mainPath.replace("https://", ""), Pattern.CASE_INSENSITIVE);
        for (String link : allLinks) {
            Matcher matcher = mainPathPattern.matcher(link);
            if (matcher.find()) {
                if (!nativeLinks.contains(link)) {
                    nativeLinks.add(link);
                    //System.out.println("Update link: " + link);
                    if (!isPageCreated(link)) {
                        engineLinkService(link);
                    }
                }

            } else if (!link.contains("http")) {//((!link.contains("https://")) || (!link.contains("http://")))
                String updatedLink = mainPath + link; //
                if (!nativeLinks.contains(updatedLink) || (!updatedLink.equals(currentPath))) {
                    nativeLinks.add(updatedLink);
                    //System.out.println("Updated elseIf link: " + updatedLink);
                    if (!isPageCreated(updatedLink)) {
                        engineLinkService(updatedLink);
                    }
                }

            } else {
                foreignLinksCounter++;
            }
        }
        return nativeLinks;
    }
    public int checkDeep(String currentPath) {
        if (currentPath.equals(mainPath)) {
            return 1;
        }
        String different = currentPath.replace(mainPath, "");
        String[] split = different.split("/");
        int deep = split.length;
        return deep;
    }

    public boolean isPageCreated(String path) {
        for (Page list : listOfPages) {
            if (list.pathName.equals(path)) {
                return true;
            }
        }
        return false;
    }
}
