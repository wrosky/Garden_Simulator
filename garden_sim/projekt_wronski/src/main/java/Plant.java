public class Plant extends Entity {

    private int hp;

    // Funkcje dot. rośliny i jej interakcji
    public Plant(int hp) {
        this.hp = hp;
    }

    public void damage() {
        this.hp--;
    }

    public void heal() {
        this.hp++;
    }

    public int getHp() {
        return hp;
    }
}
