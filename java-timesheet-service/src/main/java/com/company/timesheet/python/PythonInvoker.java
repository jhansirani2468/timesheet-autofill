package com.company.timesheet.python;

import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class PythonInvoker {

    private static final String PYTHON_PATH = "python"; // or python3
    private static final String SCRIPT_PATH = "python-llm-service/main.py";

    public String runPython(String issuesJson) throws Exception {

        ProcessBuilder pb = new ProcessBuilder(
                PYTHON_PATH,
                SCRIPT_PATH,
                issuesJson // JSON string argument
        );

        pb.redirectErrorStream(true);
        Process process = pb.start();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
        );

        StringBuilder output = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            output.append(line);
        }

        process.waitFor();

        return output.toString();
    }
}
