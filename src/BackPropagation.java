
public class BackPropagation //класс созданный для обучения нейронной сети.
{

    private static double lerningRate=0.01;//параметр отвечающий  за скорость обучения
    public static double makeNewWaight(double Waight, double vihod, double whish, double sumvhod, double yi)//метод рачитывающий новые веса для выходного нейрона
    {
        double delta=-lerningRate*mathematic.sigmoidDX(sumvhod)*yi*(vihod-whish);
        double newWaight=Waight+delta;
        return newWaight;
    }

    public static double makeError(double vihod, double whish, double sumvhod)//метод созданный для расчета ошикбки
    {
        double error=mathematic.sigmoidDX(sumvhod)*(vihod-whish);
        return error;
    }

    public static double makeNewWaight1(double Waight, double Waight1, double Waight2, double error1, double error2, double sumvhod, double yi)
    {

        double delta=-lerningRate*((error1*Waight1+error2*Waight2)/2)*mathematic.sigmoidDX(sumvhod)*yi;
        double newWaight=Waight+delta;
        return newWaight;
    }

    public static double makeNewWaight1(double Waight, double Waight1, double error1, double sumvhod, double yi)//метод рачитывающий новые веса относительно ошибки
    {

        double delta=-lerningRate*((error1*Waight1)/2)*mathematic.sigmoidDX(sumvhod)*yi;
        double newWaight=Waight+delta;
        return newWaight;
    }
}
