package tde3;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

// Questão 7
public class TransactionCountyByYearFlux {

    public static void main (String args[]){
        // Sets ERROR-only logging
        Logger.getLogger("org").setLevel(Level.ERROR);
        // habilita o uso de n threads
        SparkConf conf = new SparkConf().setAppName("transactionCountyByYearFlux").setMaster("local[*]");
        // cria o contexto da aplicacao
        JavaSparkContext sc = new JavaSparkContext(conf);

        // Carrega arquivo
        JavaRDD<String> rdd = sc.textFile("in/transactions.csv")
                // Remove cabeçalho
                .filter(l -> !l.startsWith("country_or_area"));

        // Criando pair RDD com chave e valor ((Ano, Fluxo), Ocorrencia)
        JavaPairRDD<YearFlux, Integer> pairRDD = rdd.mapToPair(l -> {
            // Quebrando linha original em campos
            String[] vals = l.split(";");
            String year = vals[1];
            String flux = vals[4];
            YearFlux key = new YearFlux(year, flux);
            return new Tuple2<>(key, 1);
        })
        // Soma todas as ocorrencias de cada chave
        .reduceByKey((x,y) -> x+y);

        // Escreve em arquivo
        pairRDD.coalesce(1).saveAsTextFile("output/TransactionCountyByYearFlux");
    }

}
