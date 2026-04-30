package Armee;

public abstract class Armee {

    protected int force;
    private final static int FORCE_DEFAUT = 10;
    private final static int FORCE_MIN = 1;


    public Armee(int force) {
        setForce(force);
    }

    public Armee() {
        this(FORCE_DEFAUT);
    }

    public int getForce() {
        return force;
    }

    public void setForce(int force) {
        if (!isValidForce(force)) {
            throw new IllegalArgumentException("Force invalide");
        } else {
            this.force = force;
        }
    }

    public boolean isValidForce(int force) {
        if (force < FORCE_MIN) {
            return false;

        } else {
            return true;

        }


    }

}
