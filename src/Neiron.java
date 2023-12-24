import java.lang.Math;
public class Neiron//класс созданый для эмитации работы нейрона
{
    private double  sumvhod;
    private int sloi, neironNumber;
    private int i;

    public Neiron(double sumvhod, int sloi, int neironNumber)
    {
        this.sumvhod = sumvhod;
        this.sloi = sloi;
        this.neironNumber = neironNumber;
    }



    public double out()//метод расчитывающий выход нейрона
    {
        if(sloi!=2)
        {
                return mathematic.sigmoid(sumvhod);
        }
        else
        {
            double out =1000* mathematic.sigmoid(sumvhod);
            return out;
        }
//        else
//        {
//            double out =200* matematic.sigmoid(sumvhod);
//            return out;
//        }
    }
}
