//autor - Szymon Czudowski - s26858
//W grze biorą udział dwaj generałowie ze swoimi armiami. Każdy generał posiada armię żołnierzy oraz worek ze złotymi monetami.
//Żołnierze posiadają: - stopień wojskowy: 
//szeregowy (wartość: 1), kapral (wartość: 2), kapitan (wartość: 3) i major (wartość: 4)
//doświadczenie - siła żołnierza jest obliczana jako iloczyn jego stopnia i doświadczenia - żołnierz ginie, gdy jego doświadczenie = 0 - jeżeli doświadczenie osiągnie pięciokrotność wartości stopnia, awansuje na kolejny stopień oraz jego doświadczenie = 1.
//Generałowie posiadają początkową (ograniczoną) liczbę złotych monet. Celem generała jest posiadanie największej i najlepiej wyszkolonej armii.

//Generał może: - zarządzić manewry swojej armii (lub jej części), które powiększają doświadczenie uczestniczących w nich żołnierzy o 1; 
//manewry kosztują: za każdego żołnierza biorącego udział w manewrach generał płaci wartość (liczbę monet) przypisaną do stopnia wojskowego - zaatakować drugiego generała; wygrywa generał, który posiada armię o większej łącznej sile; przegrany przekazuje 10% swojego złota wygrywającemu; 
//każdy żołnierz z armii przegrywającej traci 1 punkt doświadczenia, a z wygrywającej zyskuje jeden; w przypadku remisu każdy generał musi rozstrzelać jednego swojego losowo wybranego żołnierza - kupić żołnierzy; koszt żołnierza = 10 *(jego stopień); zakupieni żołnierze posiadają doświadczenie = 1

//Walczącym generałom przygląda się sekretarz prezydenta. Pisze on raporty dotyczące obu armii. Opisuje wszelkie akcje podjęte przez generałów oraz zmiany poszczególnych żołnierzy. Generał wraz ze swoimi zasobami powinien mieć możliwość zapisu i odczytu swojego stanu na / z dysku.



import java.util.Random;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class Generals {
    private String name;
    private int gold;
    private int soldiers;
    private int experience;

    public Generals(String name) {
        this.name = name;
        this.gold = 500;
        this.soldiers = 200;
        this.experience = 0;
    }

    public String getName() {
        return name;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getSoldiers() {
        return soldiers;
    }

    public void setSoldiers(int soldiers) {
        this.soldiers = soldiers;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void performManeuvers() {
        System.out.println(name + " wykonuje manewry.");
        soldiers += 30;
        gold -= 60;
        experience += 1;
        System.out.println("Armia " + name + " otrzymuje 30 żołnierzy, traci 60 złota i zdobywa 1 punkt doświadczenia.");
        saveEvent(name + " wykonuje manewry.");
    }

    public void performAttack(Generals opponent) {
        System.out.println(name + " wykonuje atak.");
        Random random = new Random();
        int army1 = soldiers + experience;
        int army2 = opponent.getSoldiers() + opponent.getExperience();

        if (army1 > army2) {
            int remainingSoldiers = (int) (0.8 * soldiers);
            int goldPayment = (int) (0.1 * gold);
            setSoldiers(remainingSoldiers);
            setGold(gold - goldPayment);
            opponent.setSoldiers(opponent.getSoldiers() - (opponent.getSoldiers() / 5));
            opponent.setGold(opponent.getGold() + goldPayment);
            opponent.setExperience(opponent.getExperience() + 2);
            System.out.println(name + " wygrał bitwę. " + opponent.getName() + " stracił 20% żołnierzy. " + goldPayment + " złota zostało przekazane do " + name + ".");
            saveEvent(name + " wygrał bitwę z " + opponent.getName() + ".");
        } else if (army1 < army2) {
            int remainingSoldiers = (int) (0.8 * opponent.getSoldiers());
            int goldPayment = (int) (0.1 * opponent.getGold());
            setSoldiers(soldiers - (soldiers / 5));
            setGold(gold + goldPayment);
            opponent.setSoldiers(remainingSoldiers);
            opponent.setGold(opponent.getGold() - goldPayment);
            opponent.setExperience(opponent.getExperience() + 2);
            System.out.println(opponent.getName() + " wygrał bitwę. " + name + " stracił 20% żołnierzy. " + goldPayment + " złota zostało przekazane do " + opponent.getName() + ".");
            saveEvent(opponent.getName() + " wygrał bitwę z " + name + ".");
        } else {
            System.out.println("Bitwa zakończyła się remisem.");
            saveEvent("Bitwa zakończyła się remisem.");
        }
    }

    public void purchaseSoldiers() {
        System.out.println(name + " dokonuje zakupu żołnierzy.");
        soldiers += 30;
        gold -= 60;
        saveEvent(name + " dokonuje zakupu żołnierzy.");
    }

    public void saveEvent(String event) {
        try {
            FileWriter writer = new FileWriter("events.txt", true);
            writer.write(event + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Wystąpił błąd podczas zapisu do pliku.");
        }
    }

    public static void main(String[] args) {
        Generals general1 = new Generals("Generał 1");
        Generals general2 = new Generals("Generał 2");

        System.out.println("Początkowe stany generałów:");
        System.out.println(general1.getName() + " posiada " + general1.getGold() + " złota i " + general1.getSoldiers() + " żołnierzy.");
        System.out.println(general2.getName() + " posiada " + general2.getGold() + " złota i " + general2.getSoldiers() + " żołnierzy.");

        Scanner scanner = new Scanner(System.in);
        String choice;

        do {
            // Wykonuje czynność dla Generała 1
            System.out.println("\nKolej Generała 1: Wybierz czynność (Manewry/Atak/Zakup żołnierzy/Zapisz/Wczytaj/Koniec): ");
            choice = scanner.nextLine();
            switch (choice.toLowerCase()) {
                case "manewry":
                    general1.performManeuvers();
                    break;
                case "atak":
                    general1.performAttack(general2);
                    break;
                case "zakup żołnierzy":
                    general1.purchaseSoldiers();
                    break;
                case "zapisz":
                    saveGame(general1, general2);
                    break;
                case "wczytaj":
                    loadGame(general1, general2);
                    break;
                case "koniec":
                    break;
                default:
                    System.out.println("Nieprawidłowa opcja.");
            }

            // Wykonuje czynność dla Generała 2
            System.out.println("\nKolej Generała 2: Wybierz czynność (Manewry/Atak/Zakup żołnierzy/Zapisz/Wczytaj/Koniec): ");
            choice = scanner.nextLine();
            switch (choice.toLowerCase()) {
                case "manewry":
                    general2.performManeuvers();
                    break;
                case "atak":
                    general2.performAttack(general1);
                    break;
                case "zakup żołnierzy":
                    general2.purchaseSoldiers();
                    break;
                case "zapisz":
                    saveGame(general1, general2);
                    break;
                case "wczytaj":
                    loadGame(general1, general2);
                    break;
                case "koniec":
                    break;
                default:
                    System.out.println("Nieprawidłowa opcja.");
            }
        } while (!choice.equalsIgnoreCase("koniec"));

        scanner.close();
    }

    public static void saveGame(Generals general1, Generals general2) {
        try {
            FileWriter writer = new FileWriter("savegame.txt");
            writer.write(general1.getName() + "," + general1.getGold() + "," + general1.getSoldiers() + "," + general1.getExperience() + "\n");
            writer.write(general2.getName() + "," + general2.getGold() + "," + general2.getSoldiers() + "," + general2.getExperience() + "\n");
            writer.close();
            System.out.println("Gra została zapisana.");
        } catch (IOException e) {
            System.out.println("Wystąpił błąd podczas zapisu gry.");
        }
    }

    public static void loadGame(Generals general1, Generals general2) {
        try {
            FileReader reader = new FileReader("savegame.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            int lineNumber = 1;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    if (lineNumber == 1) {
                        general1.setGold(Integer.parseInt(data[1]));
                        general1.setSoldiers(Integer.parseInt(data[2]));
                        general1.setExperience(Integer.parseInt(data[3]));
                    } else if (lineNumber == 2) {
                        general2.setGold(Integer.parseInt(data[1]));
                        general2.setSoldiers(Integer.parseInt(data[2]));
                        general2.setExperience(Integer.parseInt(data[3]));
                    }
                }
                lineNumber++;
            }
            bufferedReader.close();
            reader.close();
            System.out.println("Gra została wczytana.");
        } catch (IOException e) {
            System.out.println("Wystąpił błąd podczas wczytywania gry.");
        }
    }
}
