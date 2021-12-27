package com.pilnyck;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkService {

    String patternString = "href\\s*=\\s*(\"[^\"]*\"|[^\\s>]*)\\s*";
    Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);

    String mainPath;
    Pattern mainPathPattern;

    public LinkService(String mainPath) {
        this.mainPath = mainPath;
        mainPathPattern = Pattern.compile(mainPath, Pattern.CASE_INSENSITIVE);
    }

    List<String> nativeLinks = new ArrayList<>();
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
        nativeLinks = getAllLinks(content, currentPath);
        int deep = checkDeep(currentPath);

        pageEntity.setDeepLevel(deep);
        pageEntity.setCountForeignLinks(foreignLinksCounter);

        nativeLinksCounter = nativeLinks.size();
        return listOfPages;
    }

    public List<String> getAllLinks(String content, String currentPath) {
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

            String extensionPatternString = "(.png)|(.css)|(.ico)|(.pdf)|(.jpeg)|(.gif)|(.txt)"; //.txt
            Pattern extensionPattern = Pattern.compile(extensionPatternString, Pattern.CASE_INSENSITIVE);
            Matcher extensionMatcher = extensionPattern.matcher(updateWithDotsLink);

            Matcher linkMatcher = mainPathPattern.matcher(updateWithDotsLink);

            if (extensionMatcher.find()) {
                continue;
            }
            if (linkMatcher.find()) {
                if (updateWithDotsLink.equals(mainPath)||(updateWithDotsLink.equals(currentPath))){
                    continue;
                }
                if (!nativeLinks.contains(updateWithDotsLink)) {
                    nativeLinks.add(updateWithDotsLink);
                    if (!isPageCreated(updateWithDotsLink)) {
                        engineLinkService(updateWithDotsLink);
                    }
                }
            } else if (updateWithDotsLink.startsWith("/*")) {
                String updateString = mainPath + updateWithDotsLink.substring(1);
                if (!nativeLinks.contains(updateString)) {
                    nativeLinks.add(updateString);
                    if (!isPageCreated(updateString)) {
                        engineLinkService(updateString);
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
            return 0;
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