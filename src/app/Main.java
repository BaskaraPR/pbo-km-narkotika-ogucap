package app;

import controller.KnowledgeController;
import model.KnowledgeRepository;
import view.ConsoleView;

public class Main {

    public static void main(String[] args) {

        KnowledgeRepository repository = new KnowledgeRepository();

        KnowledgeController controller = new KnowledgeController(repository);

        ConsoleView view = new ConsoleView(controller);

        view.start();

    }

}