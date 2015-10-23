package wcex;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils; // this is our third-party lib
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.util.GenericOptionsParser;

/* Simple word-count example, based off of the example on page 42 of
 * Apress' ProHadoop (Sameer Wadkar, Madhu Siddalingaiah, Jason Venner)
 *
 * My code is a bit different because I don't assume there is already one
 * word per line, plus I use some Java 8 -isms, and I get rid of commas (in
 * an expensive hacky way; I was too lazy to figure out the right way to
 * strip only trailing commas)
 */

public class WC extends Configured implements Tool {
  
    public static class MyMapper extends
      Mapper<LongWritable, Text, Text, IntWritable> {
        
        private final static IntWritable one = new IntWritable(1);

        public void map(LongWritable lineno, Text value, Context context)
                throws IOException, InterruptedException {
            String line = value.toString();
            // hadoop has a tokenizer that's better for this, but to keep the
            // example simple I'm using builtins. Plus it gives me a chance
            // to use some streams.
            String spaceSeparated = Arrays.asList(line.split(","))
                .stream()
                .collect(Collectors.joining(" "));
            List<String> words = Arrays.asList(spaceSeparated.split(" "))
                .stream()
                .filter(s -> s.length() > 0)
                .collect(Collectors.toList());
            // Java won't easily let me use a .forEach here because of the
            // IOException (it requires functions to declare checked
            // exceptions, so a lambda can't throw them)
            for (String word : words) {
                context.write(new Text(word), one);
            }
        }
    }

    public static class MyReducer extends
      Reducer<Text, IntWritable, Text, IntWritable> {

        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            String word = key.toString();
            if (StringUtils.isAlphanumeric(word)) {
                int sum = 0;
                for (IntWritable val: values) {
                    sum += val.get();
                }
                context.write(key, new IntWritable(sum));
            }
        }
    }

    public int run(String[] allArgs) throws Exception {
        Job job = Job.getInstance(getConf());
        job.setJarByClass(WC.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.setMapperClass(MyMapper.class);
        job.setReducerClass(MyReducer.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        String[] args = new GenericOptionsParser(getConf(), allArgs)
             .getRemainingArgs();

        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.submit();
        return 0;
    }

    public static void main(String[] args) throws Exception {
      Configuration conf = new Configuration();
      ToolRunner.run(new WC(), args);
    }
}
