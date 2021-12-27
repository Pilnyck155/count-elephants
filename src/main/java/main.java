
import com.pilnyck.*;
import java.util.List;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the host: ");
        String path = scanner.nextLine();

        LinkService linkService = new LinkService(path);
        List<Page> pageList = linkService.engineLinkService(path);
        int foreignLinks = 0;
        for (Page page : pageList) {
            System.out.println(page);
            foreignLinks += page.getCountForeignLinks();
        }
        System.out.println("Domain: " + path + ", foreign links: " + foreignLinks + ", count of native pages: " + pageList.size());
    }
}
