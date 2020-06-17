package tde3;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

// Questão 3
public class HighestImportedBrazilCommodity2016 {

    public static void main (String args[]) {
        // Sets ERROR-only logging
        Logger.getLogger("org").setLevel(Level.ERROR);
        // habilita o uso de n threads
        SparkConf conf = new SparkConf().setAppName("highestImportedBrazilCommodity2016").setMaster("local[*]");
        // cria o contexto da aplicacao
        JavaSparkContext sc = new JavaSparkContext(conf);

        // Carrega arquivo
        JavaRDD<String> rdd = sc.textFile("in/transactions.csv")
                // Filtrando apenas linhas do Brasil, ano de 2016 e quantidade valida
                .filter(l -> {
                    String[] vals = l.split(";");
                    return (vals[0].equals("Brazil") && vals[1].equals("2016") && (vals[4].equals("Import")) && (!vals[8].isEmpty()));
                });

        // Criando pair RDD com chave e valor (flow, (Mercadoria, Quantidade))
        JavaPairRDD<String, CommodityQuantity> pairRDD = rdd.mapToPair(l -> {
            // Quebrando linha original em campos
            String[] vals = l.split(";");
            String commodity = vals[3];
            String flow = vals[4];
            Float quantidade = Float.parseFloat(vals[8]);
            // Classe criada correspondente a (Mercadoria, Quantidade)
            CommodityQuantity value = new CommodityQuantity(commodity, quantidade);
            return new Tuple2<>(flow, value);
        })
        // Retorna apenas a mercadoria com maior quantidade
        .reduceByKey((x, y) -> {
            if (x.getQuantity() < y.getQuantity()) {
                return y;
            }
            return x;
        });

        // Escreve em arquivo
        pairRDD.coalesce(1).saveAsTextFile("output/HighestImportedBrazilCommodity2016");
    }

}
