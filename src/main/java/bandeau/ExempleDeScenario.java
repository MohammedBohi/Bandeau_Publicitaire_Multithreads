package bandeau;

public class ExempleDeScenario {

    /**
     * "Programme principal" : point d'entrée d'exécution
     *
     * @param args les "arguments de ligne de commande", transmis au lancement du programme
     */
    public static void main(String[] args) {
        ExempleDeScenario instance = new ExempleDeScenario();
        instance.executeScenario();
        }
    public void executeScenario() {
        // Création des bandeaux
        BandeauVerrouillable b1 = new BandeauVerrouillable();
        BandeauVerrouillable b2 = new BandeauVerrouillable();
        BandeauVerrouillable b3 = new BandeauVerrouillable();

        System.out.println("CTRL-C pour terminer le programme");

        // Création du scénario avec les effets
        Scenario s = createScenario();

        // Création de threads pour exécuter le scénario en parallèle sur chaque bandeau
        Thread thread1 = new Thread(() -> {
            while (true) { // Boucle infinie pour redémarrer le scénario sur b1
                b1.lock();
                try {
                    s.playOn(b1);
                } finally {
                    b1.unlock();
                }
            }
        });


        Thread thread2 = new Thread(() -> {
            b2.lock();
            try {
                s.playOn(b2);
            } finally {
                b2.unlock();
            }
        });

        Thread thread3 = new Thread(() -> {
            b3.lock();
            try {
                s.playOn(b3);
            } finally {
                b3.unlock();
            }
        });

        // Démarrage des threads
        thread1.start();
        thread2.start();
        thread3.start();

        try {
            // Attendre que chaque thread termine
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Scenario createScenario() {
        Scenario s = new Scenario();

        //  effets
        s.addEffect(new RandomEffect("Le jeu du pendu", 700), 1);
        s.addEffect(new Rainbow("Comme c'est joli !", 30), 1);
        s.addEffect(new Rotate("2 tours à droite", 180, 4000, true), 2);

        return s;
    }
}
