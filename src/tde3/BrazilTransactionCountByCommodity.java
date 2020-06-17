package tde3;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

// Questão 1
public class BrazilTransactionCountByCommodity {

    public static void main (String args[]) {
        // Sets ERROR-only logging
        Logger.getLogger("org").setLevel(Level.ERROR);
        // habilita o uso de n threads
        SparkConf conf = new SparkConf().setAppName("brazilTransactionCountByCommodity").setMaster("local[*]");
        // cria o contexto da aplicacao
        JavaSparkContext sc = new JavaSparkContext(conf);

        // Carrega arquivo
        JavaRDD<String> rdd = sc.textFile("in/transactions.csv")
                // Remove cabeçalho (desnecessário pelo filter)
                .filter(l -> !l.startsWith("country_or_area"))
                // Filtrando apenas linhas do Brasil
                .filter(l -> l.split(";")[0].equals("Brazil"));

        // Criando pair RDD com chave e valor (Mercadoria, Ocorrencia)
        JavaPairRDD<String, Integer> pairRDD = rdd.mapToPair(l -> {
            // Quebrando linha original em campos
            String[] vals = l.split(";");
            String commodity = vals[3];
            return new Tuple2<>(commodity, 1);
        })
        // Função lambda que soma dodos os Integer de cada key (conta ocorrencias)
        .reduceByKey(Integer::sum);

        // Escreve em arquivo
        pairRDD.coalesce(1).saveAsTextFile("output/BrazilTransactionCountByCommodity");
    }

}
