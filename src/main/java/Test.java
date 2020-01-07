import reader.IReaderDBService;
import reader.ReaderDBServiceImpl;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.*;

public class Test {

    public static void main(String args[]) {

        IReaderDBService readerDBService = new ReaderDBServiceImpl();
        List<Integer> list = readerDBService.getReadersCards();

        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }
}