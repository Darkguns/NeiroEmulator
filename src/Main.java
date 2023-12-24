import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
//перед чтением проекта важно понимать, что нейроэмулятор является нейронной сетью эмитирующей работу токарного станка. Данная нейронная сеть написана для обучения другой нейронной сети "neironet" они созданны работать в паре.
public class Main
{

    public static void main(String[] args)
    {
        int kolvosloev=7, kolvoneiron=7, countNeironVhod=2, countNeironVihod=1;//переменные отвечающие за структуру нейронной сети
        double T, F, U, count=10000;//переменные требующиеся для обучения нейронной сети
        double[][][] Waight = new double[kolvosloev][countNeironVhod][kolvoneiron];//массивы старых весов нейронки
        double[][][] newWaight = new double[kolvosloev][countNeironVhod][kolvoneiron];//массивы новых весов нейронки
        String SWaight;

        try {

                Path pathT = Path.of("D:\\Учеба\\Диплом\\T.txt");//нужно прописать путь к этим файлам(T.txt, E.txt, U.txt,F.txt, NeuroemulatorWaight.txt")
                Path pathU = Path.of("D:\\Учеба\\Диплом\\U.txt");
                Path pathF = Path.of("D:\\Учеба\\Диплом\\F.txt");
                Path path = Path.of("D:\\Учеба\\Диплом\\NeuroemulatorWaight.txt");
                String ST = Files.readString(pathT);
                String SU = Files.readString(pathU);
                String SF = Files.readString(pathF);
                String[] arrayFromStringT = ST.split(" ");//Парсим значения из файлов
                String[] arrayFromStringU = SU.split(" ");
                String[] arrayFromStringF = SF.split(" ");
                double[] arrayT=new double[arrayFromStringT.length];//Создаем массивы для значений из файлов
                double[] arrayU=new double[arrayFromStringU.length];
                double[] arrayF=new double[arrayFromStringF.length];
                for (int i = 0; i < arrayFromStringT.length; i++)// значения записываем в массивы
                {
                    arrayT[i] = Double.parseDouble(arrayFromStringT[i]);
                }
                for (int i = 0; i < arrayFromStringU.length; i++)
                {
                    arrayU[i] = Double.parseDouble(arrayFromStringU[i]);
                }
                for (int i = 0; i < arrayFromStringF.length; i++)
                {
                    arrayF[i] = Double.parseDouble(arrayFromStringF[i]);
                }
            for (int l = 0; l < count; l++)//начало обучения. Обучение будет идти столько эпох, сколько задано
            {
                for (int g = 0; g < arrayT.length; g++)//одна итерация одной эпохи
                {
                    SWaight = Files.readString(path);//считываются веса из файла
                    T=arrayT[g];//значения соответсвующие параметрам в этой итерации сохраняются
                    U=arrayU[g];
                    F=arrayF[g];
                    T=T/1000;
                    String[] arrayFromString = SWaight.split(" ");//Парсим значения из файла с весами
                    int n = 0;
                    for (int i = 0; i < countNeironVhod; i++) {
                        for (int j = 0; j < countNeironVhod; j++) {
                            for (int k = 0; k < kolvoneiron; k++) {
                                Waight[i][j][k] = Double.parseDouble(arrayFromString[n]);//каждое значение веса сохраняетсяв специальную переменную типа Waight[i][j][k], где i-значения слоя весов, j-номер нейрона из входного слоя с которым соеденен синапс, к которому принадлежит вес, k-номер нейрона выходного слоя соответсвенно.
                                n++;
                            }
                        }
                    }

                    ArrayList<Sinaps> sinapsList = new ArrayList<Sinaps>();//создаем коллекцию синапсов
                    for(int a=0; a<Waight.length; a++)
                    {
                        for(int b=0; b<Waight[0].length; b++)
                        {
                            for(int c=0; c<kolvoneiron; c++)
                            {
                                sinapsList.add(new Sinaps(Waight[a][b][c]));//создаем синапс и присваиваем каждому синопсу соответсвующий вес
                            }
                        }
                    }


                    ArrayList<Neiron> neironList = new ArrayList<Neiron>();//создаем коллекцию нейронов
                    for(int a=0; a<kolvoneiron; a++)
                    {
                                neironList.add(new Neiron(sinapsList.get(a).ToNeiron(F)+sinapsList.get(a+kolvoneiron).ToNeiron(U),1,a));//создаем нейрон и передаем в него сразу все входные данные

                    }
//
                    double sumvhod10x=0;
                    for (int a=0; a<kolvoneiron; a++)
                    {
                    sumvhod10x=sumvhod10x+sinapsList.get(kolvoneiron * 2+a).ToNeiron(neironList.get(a).out());//расчитывем чему будет равна сумма входов в выходной нейрон
                    }

                    Neiron outT = new Neiron(sumvhod10x,2, 0);//расчитываем значение выхода выходного нейрона

                    System.out.println("Значение итоговой температуры при U="+U+" и F="+F+" : T=" + outT.out());

                    double errorT=BackPropagation.makeError( outT.out(), T,  sumvhod10x);//расчитываем значения выходов из выходного нейрона

                    for(int a=0;a<kolvoneiron;a++)//рассчитываем новые веса для каждого синапса относительно ошибки
                    {
                        newWaight[1][0][a] = BackPropagation.makeNewWaight(Waight[1][0][a], outT.out(), T, sumvhod10x, neironList.get(a).out());
                    }


                    for(int a=0; a<countNeironVhod; a++)//рассчитываем новые веса для каждого синапса относительно ошибки относительно ошибок нейронов скрытых слоев
                    {
                        for (int b=0; b<kolvoneiron; b++)
                        {
                            if (a==0)
                                newWaight[0][a][b] = BackPropagation.makeNewWaight1(Waight[0][1][b],  Waight[1][0][b], errorT,  sinapsList.get(a).ToNeiron(F)+sinapsList.get(a+kolvoneiron).ToNeiron(U), F);
                            if (a==1)
                                newWaight[0][a][b] = BackPropagation.makeNewWaight1(Waight[0][1][b],  Waight[1][0][b], errorT,  sinapsList.get(a).ToNeiron(F)+sinapsList.get(a+kolvoneiron).ToNeiron(U), U);
                        }
                    }

                    SWaight = "";//записываем новые веса в файл хранящий веса
                    for (int i = 0; i < countNeironVhod; i++)
                    {
                        for (int j = 0; j < countNeironVhod; j++)
                        {
                            for (int k = 0; k < kolvoneiron; k++)
                            {
                                SWaight = SWaight + newWaight[i][j][k] + " ";
                            }
                        }
                    }
                    Files.writeString(path, SWaight);
                }
            }
            }
        catch(Exception e)
            {
                System.out.println("Возникла проблема с чтением данных с файла Waight.txt");
                e.printStackTrace();
            }

    }

}