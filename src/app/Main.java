package app;

import controller.KnowledgeController;
import model.KnowledgeRepository;
import view.SwingView;

public class Main {

    public static void main(String[] args) {

        KnowledgeRepository repository = new KnowledgeRepository();

        KnowledgeController controller = new KnowledgeController(repository);

        SwingView view = new SwingView(controller);

        view.setVisible(true);
    }

}