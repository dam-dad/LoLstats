package dad.LoLstats.objs;
/***
 * Enum to get the LP value from the Division Constants.
 * @author katarem
 * @version 1.0 March 3rd 2023
 */
public enum Divisiones {
    I(300),
    II(200),
    III(100),
    IV(0);

    public int div;
    Divisiones(int div){
        this.div = div;
    }
}
