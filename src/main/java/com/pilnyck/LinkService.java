package com.pilnyck;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkService {
    //regular expression for find links on page
    //String patternString = "href\\s*=\\s*(\"[^\"]*\"|[^\\s>]*)\\s*";
    String patternString = "href\\s*=\\s*(\"[^\"]*\"|[^\\s>]*)\\s*html";
    Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);

    String mainPath;
    Pattern mainPathPattern;

    public LinkService(String mainPath) {
        this.mainPath = mainPath;
        mainPathPattern = Pattern.compile(mainPath.replace("https://", ""), Pattern.CASE_INSENSITIVE);
    }

    public int getNativeLinksCounter() {
        return nativeLinksCounter;
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
        //List<String> allLinks = getAllLinks(content, currentPath);
        nativeLinks = getAllLinks(content, currentPath);
        //nativeLinks = getNativeLinks(allLinks, currentPath);

        int deep = checkDeep(currentPath);

        pageEntity.setDeepLevel(deep);
        pageEntity.setCountForeignLinks(foreignLinksCounter);

        nativeLinksCounter = nativeLinks.size();
        return listOfPages;
    }

    public List<String> getAllLinks(String content, String currentPath) {
        //List<String> links = new ArrayList<>();
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String match = content.substring(start, end);

            //    String linkPattern = "[(^http://)|(^http://)]";
            //String updateLindPattern = "[(^href=)(/>$)]['"][(\.\.\\/$)]"
            //String updateLindPattern = "[(^href=)|(/>$)]";
            //String updateLindPattern = "[(href=)|(/>)]";
            //Pattern patternForUpdateLink = Pattern.compile(updateLindPattern, Pattern.CASE_INSENSITIVE);
            //String updateWithDotsLink = match.replace(updateLindPattern, "");


            String updateStartLink = match.replace("href=", ""); // replaceFirst
            String updateEndLink = updateStartLink.replace("\"/>", "");
            String updateWithApostropheLink = updateEndLink.replace("'", "");
            String updateLink = updateWithApostropheLink.replace("\"", "");
            String updateWithDotsLink = updateLink.replace("../", "");

            // перевірка посилань!
            Matcher linkMatcher = mainPathPattern.matcher(updateWithDotsLink);

            if (linkMatcher.find()) {
                if (!nativeLinks.contains(updateWithDotsLink)) {
                    nativeLinks.add(updateWithDotsLink);
                    //System.out.println("Update link: " + link);
                    if (!isPageCreated(updateWithDotsLink)) {
                        engineLinkService(updateWithDotsLink);
                    }
                }
            } else if (updateWithDotsLink.startsWith("/")) {
                String cuttedLink = cutLastLink(currentPath);
                String updateString = cuttedLink + updateWithDotsLink;
                if (!nativeLinks.contains(updateString)) {
                    nativeLinks.add(updateString);
                    //System.out.println("Update link: " + link);
                    if (!isPageCreated(updateString)) {
                        engineLinkService(updateString);
                    }
                }
            }
            //} else if (!updateWithDotsLink.contains("http")) {
            else if (!updateWithDotsLink.startsWith("http")) {
                if (!equalsLinks(updateWithDotsLink, currentPath)) {
                    String updatedLink = mainPath + updateWithDotsLink;
                    //TODO: Check possible options to change update link
                    //String updatedLink = mainPath + updateWithDotsLink;
                    if (!nativeLinks.contains(updatedLink) || (!updatedLink.equals(currentPath))) {
                        nativeLinks.add(updatedLink);
                        //System.out.println("Updated elseIf link: " + updatedLink);
                        if (!isPageCreated(updatedLink)) {
                            engineLinkService(updatedLink);
                        }
                    }
                }

            }

            /*
            else if (updateWithDotsLink.startsWith("/") || (!updateWithDotsLink.contains("https://"))) {//((!link.contains("https://")) || (!link.contains("http://")))   (!link.contains("https"))
                String updatedLink = mainPath + updateWithDotsLink; //
                if (!nativeLinks.contains(updatedLink) || (!updatedLink.equals(currentPath))) {
                    nativeLinks.add(updatedLink);
                    //System.out.println("Updated elseIf link: " + updatedLink);
                    if (!isPageCreated(updatedLink)) {
                        engineLinkService(updatedLink);
                    }
                }

            }
            */
            else {
                foreignLinksCounter++;
            }
            //links.add(updateWithDotsLink);
        }
        //return links;
        return nativeLinks;
    }

    private String cutLastLink(String currentPath) {
        int index = currentPath.lastIndexOf("/");
        String updateString = currentPath.substring(0, index + 1);
        return updateString;
    }

    //if updateWithDotsLink send to current link
    private boolean equalsLinks(String updateWithDotsLink, String currentPath) {
        String[] split = currentPath.split("/");
        if (split[split.length - 1].equals(updateWithDotsLink)) {
            return true;
        }
        return false;
    }


    /*
    public List<String> getNativeLinks(List<String> allLinks, String currentPath) {
        //Pattern mainPathPattern = Pattern.compile(mainPath.replace("https://", ""), Pattern.CASE_INSENSITIVE);
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

            } else if (link.startsWith("/")) {//((!link.contains("https://")) || (!link.contains("http://")))   (!link.contains("https"))
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
     */

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
