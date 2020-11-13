# Search Engine
This project contains the implementation of a text search engine in Java using Lucene.
## Running and evaluating results
In this section I will expplain how to get the software running
### Building and Executing
There are a few steps that need to be done to run the program.

You can compile the java code from the main folder using the command:
```sh
mvn package
```

After compiling it, you can run the code using different types of analyzers and different scores approaches.
The command to do that is:
```sh
java -jar target/search_engine-1.0.jar <analyzer_name> <scoring_approach>
```

The analyzers possibilities are:
- "standard" (https://lucene.apache.org/core/8_6_3/core/org/apache/lucene/analysis/standard/StandardAnalyzer.html)
- "simple" (https://lucene.apache.org/core/8_6_3/analyzers-common/org/apache/lucene/analysis/core/SimpleAnalyzer.html)
- "english" (default) (https://lucene.apache.org/core/8_6_3/analyzers-common/org/apache/lucene/analysis/en/EnglishAnalyzer.html)

The scoring approaches are:
- "boolean" (https://lucene.apache.org/core/8_6_3/core/org/apache/lucene/search/similarities/BooleanSimilarity.html)
- "vector" (https://lucene.apache.org/core/8_6_3/core/index.html?org/apache/lucene/search/similarities/ClassicSimilarity.html)
- "BM25" (default) (https://lucene.apache.org/core/8_6_3/core/org/apache/lucene/search/similarities/BM25Similarity.html)

The analyzer marked as default is automatically picked if the first parameter do not match any of the possibility.
The scoring approach marked as default is automatically picked if the second parameter do not match any of the possibility.
If the number of parameters in not exactly 2, I use the default analyzer, and the default scoring approach.
To use the default approach, that also has the best results, the command is:
```sh
java -jar target/search_engine-1.0.jar
```

The result of running the program will be a file named query_results.txt in the main folder.
### Evaluating
To evaluate the results I have used trec_eval.
The first step is to compile the trec_eval folder:
```sh
cd trec_eval-9.0.7/trec_eval-9.0.7
make
```

Once this is done, you will be able to use trec_eval to evaluate the performance of the search engine:
```sh
./trec_eval ../../cranfield_dataset/QRelsCorrectedforTRECeval ../../query_results.txt
```
