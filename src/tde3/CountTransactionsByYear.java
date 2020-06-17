package tde3;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

// Questão 2
public class CountTransactionsByYear {

    public static void main (String args[]) {
        // Sets ERROR-only logging
        Logger.getLogger("org").setLevel(Level.ERROR);
        // habilita o uso de n threads
        SparkConf conf = new SparkConf().setAppName("countTransactionsByYear").setMaster("local[*]");
        // cria o contexto da aplicacao
        JavaSparkContext sc = new JavaSparkContext(conf);

        // Carrega arquivo
        JavaRDD<String> rdd = sc.textFile("in/transactions.csv")
                // Remove cabeçalho
                .filter(l -> !l.startsWith("country_or_area"));

        // Criando pair RDD com chave e valor (Ano, Ocorrencia)
        JavaPairRDD<String, Integer> pairRDD = rdd.mapToPair(l -> {
            // Quebrando linha original em campos
            String[] vals = l.split(";");
            String year = vals[1];
            return new Tuple2<>(year, 1);
        }).reduceByKey(Integer::sum);

        // Escreve em arquivo
        pairRDD.coalesce(1).saveAsTextFile("output/CountTransactionsByYear");
    }

}
