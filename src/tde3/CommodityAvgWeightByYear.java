package tde3;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

// Questão 4
public class CommodityAvgWeightByYear {

    public static void main (String args[]){
        // Sets ERROR-only logging
        Logger.getLogger("org").setLevel(Level.ERROR);
        // habilita o uso de n threads
        SparkConf conf = new SparkConf().setAppName("commodityAvgWeightByYear").setMaster("local[*]");
        // cria o contexto da aplicacao
        JavaSparkContext sc = new JavaSparkContext(conf);

        // Carrega arquivo
        JavaRDD<String> rdd = sc.textFile("in/transactions.csv")
                // Remove cabeçalho
                .filter(l -> (!l.startsWith("country_or_area") && (!l.split(";")[6].isEmpty())));

        // Criando pair RDD com chave e valor
        JavaPairRDD<YearCommodity, AvgWeight> pairRDD = rdd.mapToPair(l -> {
            // Quebrando linha original em campos
            String[] vals = l.split(";");
            String year = vals[1];
            String commodity = vals[3];
            Float weight = Float.parseFloat(vals[6]);
            // Classe criada correspondente a (Ano, Mercadoria)
            YearCommodity key = new YearCommodity(year, commodity);
            // Classe criada correspondente a (Peso, Quantidade) (calcula media no toString)
            AvgWeight value = new AvgWeight(weight);
            return new Tuple2<>(key, value);
        })
        // Faz a soma de peso e ocorrencias
        .reduceByKey((x, y) ->
                new AvgWeight(x.getWeight()+y.getWeight(),
                        x.getQtd()+y.getQtd()));

        // Escreve em arquivo
        pairRDD.coalesce(1).saveAsTextFile("output/CommodityAvgWeightByYear");
    }

}
