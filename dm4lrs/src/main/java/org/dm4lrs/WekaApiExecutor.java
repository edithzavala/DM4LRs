package org.dm4lrs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import weka.classifiers.Evaluation;
import weka.classifiers.rules.JRip;
import weka.classifiers.rules.Rule;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

public class WekaApiExecutor {

  public static String runDataMining(String algorithm, String fileName, String fileType,
      String classAttribute) {
    // convert file if necessary
    switch (fileType) {
      case "CSV":
        convertCsvToArff(fileName);
        break;
      default:
        break;
    }
    // execute data mining algorithm
    switch (algorithm) {
      case "JRip":
        return runJrip(fileName);
      default:
        break;
    }
    return null;
  }

  private static void convertCsvToArff(String fileName) {
    CSVLoader loader = new CSVLoader();
    try {
      // load CSV
      loader.setSource(new File(fileName + ".csv"));
      Instances data = loader.getDataSet();
      // set the dataset
      ArffSaver saver = new ArffSaver();
      saver.setInstances(data);
      // save as arff
      saver.setFile(new File(fileName + ".arff"));
      saver.writeBatch();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static String runJrip(String fileName) {
    String results = null;
    try {
      Instances dataset;
      dataset = new Instances(new BufferedReader(new FileReader(fileName + ".arff")));
      dataset.setClassIndex(dataset.numAttributes() - 1);
      JRip jr = new JRip();
      jr.buildClassifier(dataset);
      /************************* 10-fold Cross-validation ************************/
      int folds = 10;
      int seed = 1;
      Random rand = new Random(seed);
      Evaluation eval = new Evaluation(dataset);
      eval.crossValidateModel(jr, dataset, folds, rand);
      /******************************************************************/
      ArrayList<Rule> rules = jr.getRuleset();
      Map<String, String> resultsMap = new HashMap<String, String>();
      resultsMap.put("Rules", rules.toString());
      resultsMap.put("ErrorRate", String.valueOf(eval.errorRate()));
      resultsMap.put("Precision", String.valueOf(eval.precision(0)));
      resultsMap.put("Recall", String.valueOf(eval.recall(0)));
      resultsMap.put("fMeasure", String.valueOf(eval.fMeasure(0)));
      results = resultsMap.toString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return results;
  }
}
