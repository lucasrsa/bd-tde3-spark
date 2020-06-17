package tde3;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.Arrays;

// Questão ?
public class BaseRDD {

    public static void main (String args[]){
        // Sets ERROR-only logging
        Logger.getLogger("org").setLevel(Level.ERROR);
        // habilita o uso de n threads
        SparkConf conf = new SparkConf().setAppName("basic").setMaster("local[*]");
        // cria o contexto da aplicacao
        JavaSparkContext sc = new JavaSparkContext(conf);

        // Carrega arquivo
        JavaRDD<String> rdd = sc.textFile("in/transactions.csv")
                // Remove cabeçalho
                .filter(l -> !l.startsWith("country_or_area"));

        // Criando pair RDD com chave e valor
        JavaPairRDD<String, String> pairRDD = rdd.mapToPair(l -> {
            // Quebrando linha original em campos
            String[] vals = l.split(";");
            return new Tuple2<>("_1", "_2");
        });

        // Do whatever

        // Escreve em arquivo
        pairRDD.coalesce(1).saveAsTextFile("output/NOMEARQUIVO");
    }

}
