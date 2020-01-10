package reader;

import user.User;

public class Reader extends User {

    private int idReader;
    private int idUser;

    public Reader(){}

    public int getIdReader() {return idReader; }

    public void setIdReader(int idReader) {
        this.idReader = idReader;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName() + " " + getCardNumber();
    }
}

