package tde3;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;

// Questão 6
public class MostExpensiveCommodityByWeight {

    public static void main (String args[]){
        // Sets ERROR-only logging
        Logger.getLogger("org").setLevel(Level.ERROR);
        // habilita o uso de n threads
        SparkConf conf = new SparkConf().setAppName("mostExpensiveCommodityByWeight").setMaster("local[*]");
        // cria o contexto da aplicacao
        JavaSparkContext sc = new JavaSparkContext(conf);

        // Carrega arquivo
        JavaRDD<String> rdd = sc.textFile("in/transactions.csv")
                // Remove cabeçalho, garante que campos float não estão vazios (ALL COMMODITIES removido por resultar em infinity)
                .filter(l -> (!l.startsWith("country_or_area") && !l.split(";")[3].equals("ALL COMMODITIES")
                        && !l.split(";")[5].isEmpty() && !l.split(";")[6].isEmpty()));

        // Criando pair RDD com chave e valor (Mercadoria, (Preco, Peso))
        JavaPairRDD<String, PriceWeight> pairRDD = rdd.mapToPair(l -> {
            // Quebrando linha original em campos
            String[] vals = l.split(";");
            String commodity = vals[3];
            Float price = Float.parseFloat(vals[5]);
            Float weight = Float.parseFloat(vals[6]);
            PriceWeight value = new PriceWeight(price, weight);
            return new Tuple2<>(commodity, value);
        })
        // Soma todos os Precos e Pesos de uma mesma Mercadoria
        .reduceByKey((x,y) -> new PriceWeight(x.getPrice()+y.getPrice(),x.getWeight()+y.getWeight()));

        // Retorna apenas a Mercadoria de maior Preco/Peso
        Tuple2<String, PriceWeight> tupla = pairRDD.reduce((x,y) -> {
            if (x._2.getPrice()/x._2.getWeight() < y._2.getPrice()/y._2.getWeight()) {
                return y;
            }
            return x;
        });

        // Escreve em arquivo mantendo padrao das outras classes
        sc.parallelize(Arrays.asList(tupla)).coalesce(1).saveAsTextFile("output/MostExpensiveCommodityByWeight");
    }

}
