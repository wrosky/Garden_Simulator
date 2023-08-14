public class Gardener extends Entity {

    public void heal(Plant plant) {
        plant.heal();
    } // Leczenie rośliny

    public void kill(Pest pest) {
        pest = null;
    } // Zabijanie szkodnika
}
