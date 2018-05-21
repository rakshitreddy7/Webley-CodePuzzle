import java.util.*;
import java.io.*;
class Combinations {
    private List<List<Double>> result;
    private ArrayList<ArrayList<String>> result_items=new ArrayList<ArrayList<String>>();
    HashMap<Double,String> items_price_map=new HashMap<Double,String>();
    public Combinations(){
        result=new ArrayList<>();
    }
    public void combinations(double[] prices,double targetPrice){
        Map<Double,Integer> map=new HashMap<>();
        double[] bufferArray=new double[10];
        for (int i=0; i<prices.length; i++) {
            map.put(Math.round(prices[i]*1000.0)/1000.0,1);    
        }
        double[] priceList=new double[map.size()];
        int[] countOfPrices=new int[map.size()];
        int i=0;
        Iterator itr=map.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry<Double,Integer> me=(Map.Entry<Double,Integer>)itr.next();
            double price=Math.round(me.getKey()*1000.0)/1000.0;
            priceList[i]= price;
            countOfPrices[i]=Integer.MAX_VALUE;
            i++;
        }
        backtrack(priceList,countOfPrices,bufferArray,0,0,targetPrice);
    }

    void backtrack(double[] priceList,int[] countOfPrices,double[] bufferArray,int position,int level,double targetPrice){
        if(targetPrice==0.00){
           result.add(convertToList(bufferArray,level));
           return;
        }
        if(targetPrice<0.00){
            return ;
        }
        for (int i=position; i<priceList.length; i++) {
            bufferArray[level]=priceList[i];
            countOfPrices[i]--;
            backtrack(priceList,countOfPrices,bufferArray,i,level+1,Math.round((targetPrice-priceList[i])*1000.0)/1000.0);
            countOfPrices[i]++;
        }
    }
    List<Double> convertToList(double[] array,int level){
        List<Double> list=new ArrayList<>();
        for (int i=0; i<level; i++) {
            list.add(array[i]);
        }
        return list;
    }

   public List<List<Double>> combinationSum(double[] prices, double targetPrice) {
       combinations(prices,targetPrice);
       return result;
   }
   public static void main(String[] args){
        Combinations c=new Combinations();
        int rowcount=0;
        String target_price="0.0";
        String line;
        ArrayList<Double> prices=new ArrayList<Double>();
        try
        {
            BufferedReader br=new BufferedReader(new FileReader(args[0]));
            while((line =br.readLine())!=null)
            {
                line=line.replace("\"","");
                String[] items_prices=line.split(",");
                if(rowcount==0)
                {
                    target_price= items_prices[1].substring(2);
                    rowcount++;
                }
                else if(rowcount==1)
                {
                    rowcount++;
                    continue;
                }
                else{
                c.items_price_map.put(Double.parseDouble(items_prices[1].substring(1)),items_prices[0]);
                prices.add(Double.parseDouble(items_prices[1].substring(1)));
                rowcount++;
                }
            }
            double[] price_array=new double[prices.size()];
            for(int p=0;p<prices.size();p++)
                price_array[p]=prices.get(p);
            c.combinationSum(price_array,Double.parseDouble(target_price));
            File fw=new File("final_combinations.csv");
            BufferedWriter bw=new BufferedWriter(new FileWriter(fw));
            for(int j=0;j<c.result.size();j++)
            {
                ArrayList<String> temp_list=new ArrayList<String>();
                for(Double price:c.result.get(j))
                {
                    bw.write(c.items_price_map.get(price)+",");
                    temp_list.add(c.items_price_map.get(price));
                }
                bw.write("\n");
                c.result_items.add(temp_list);
            }
            bw.close();
            System.out.println(c.result_items);
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
                System.out.println("Index out of Bounds");
        }
        catch(NumberFormatException e )
        {
            System.out.println("Number Format Exception");
        }
       

   }
}
