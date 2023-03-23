package ru.iets;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.NumberFormatter;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import static ru.iets.Default.DENSITY;

public class ScreenForm {

    private static final DecimalFormat FORMATTER;

    private static final int DELAY = 5;

    static {
        DecimalFormat formatter = new DecimalFormat("0.0##");
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        FORMATTER = formatter;
    }

    public JTabbedPane tabbedPane1;
    public JPanel panel1;
    public JPanel textPanel;
    public JPanel buttonsPanel;
    public JPanel propsPanel;
    public JPanel graphPanel;
    public JPanel textPanel1;
    public JPanel textPanel2;
    public JPanel textPanel3;
    public JPanel textPanel4;
    public JPanel textPanel5;
    public JPanel textPanel6;
    public JPanel textPanel7;
    public JPanel textPanel8;
    public JPanel textPanel9;
    public JPanel textPanel10;
    public JPanel textPanel11;
    public JPanel textPanel12;
    public JFormattedTextField textField1;
    public JFormattedTextField textField2;
    public JFormattedTextField textField3;
    public JFormattedTextField textField4;
    public JFormattedTextField textField5;
    public JFormattedTextField textField6;
    public JFormattedTextField textField7;
    public JFormattedTextField textField8;
    public JFormattedTextField textField9;
    public JFormattedTextField textField10;
    public JFormattedTextField textField11;
    public JFormattedTextField textField12;
    public JLabel textLable1;
    public JLabel textLable2;
    public JLabel textLable3;
    public JLabel textLable4;
    public JLabel textLable5;
    public JLabel textLable6;
    public JLabel textLable7;
    public JLabel textLable8;
    public JLabel textLable9;
    public JLabel textLable10;
    public JLabel textLable11;
    public JLabel textLable12;
    public JButton acceptButton;
    public JButton resetButton;
    private JPanel drawingPanel;
    private JPanel buttonsPanel2;
    private ChartPanel chartPanel1;
    private JButton runButton;
    private JSlider timeSlider;
    private JButton restartButton;
    private JLabel speedText;
    private final Timer timer;
    private Computer computer;
    private boolean run = false;
    private double timeSpeed = 1.0;

    private ScreenForm(Computer computer) {
        this.computer = computer;
        $$$setupUI$$$();
        this.timer = new Timer(DELAY, e -> {
            this.computer.computeTemperatureField();
            refreshPlot(this.computer);
        });
        JFormattedTextField[] textFields = {
                textField1,
                textField2,
                textField3,
                textField4,
                textField5,
                textField6,
                textField7,
                textField8,
                textField9,
                textField10,
                textField12
        };
        for (JFormattedTextField t : textFields) {
            t.setFormatterFactory(new JFormattedTextField.AbstractFormatterFactory() {
                @Override
                public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField tf) {
                    return new NumberFormatter(FORMATTER);
                }
            });
        }
        textField11.setFormatterFactory(new JFormattedTextField.AbstractFormatterFactory() {
            // that is integer for sure
            @Override
            public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField tf) {
                return new NumberFormatter(NumberFormat.getNumberInstance());
            }
        });
        setDefaultValues();
    }

    public static void initGUI(Computer defaultValues) {
        JFrame frame = new JFrame("Temperature Field");
        ScreenForm fields = new ScreenForm(defaultValues);
        frame.setContentPane(fields.tabbedPane1);
        fields.tabbedPane1.addChangeListener(e -> {
            frame.pack();
            fields.setRun(false);
            frame.setResizable(fields.tabbedPane1.getSelectedIndex() == 1);
        });
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fields.resetButton.addActionListener(e -> {
            fields.setDefaultValues();
        });

        fields.acceptButton.addActionListener(e -> {
            fields.computer = new Computer(
                    Integer.parseUnsignedInt(fields.textField11.getText()),
                    Double.parseDouble(fields.textField12.getText()),
                    Double.parseDouble(fields.textField1.getText()),
                    Double.parseDouble(fields.textField2.getText()), // env start temp considered as flow start temp
                    Double.parseDouble(fields.textField2.getText()),
                    Double.parseDouble(fields.textField3.getText()),
                    Double.parseDouble(fields.textField4.getText()),
                    Double.parseDouble(fields.textField5.getText()),
                    Double.parseDouble(fields.textField6.getText()),
                    Double.parseDouble(fields.textField7.getText()),
                    Double.parseDouble(fields.textField8.getText()),
                    Double.parseDouble(fields.textField9.getText()),
                    Double.parseDouble(fields.textField10.getText()),
                    DENSITY // considered hardcoded
            );
            fields.refreshPlot(fields.computer.getTemperatureField(), fields.computer);
            fields.tabbedPane1.setSelectedIndex(1);
        });

        fields.runButton.addActionListener(e -> {
            fields.setRun(!fields.run);
        });
        fields.restartButton.addActionListener(e -> {
            fields.refreshPlot(fields.computer.restartTemperatureFiled(), fields.computer.getInnerRadius(), fields.computer.getRadiusStep(), fields.computer.getTimePassed());
            fields.setRun(false);
        });

        fields.timeSlider.addChangeListener(l -> {
            fields.timeSpeed = Math.pow(2, 4 - fields.timeSlider.getValue());
            fields.speedText.setText("x" + Math.pow(2, fields.timeSlider.getValue() - 4));
            fields.timer.setDelay(Math.round((float) (DELAY * fields.timeSpeed)));
        });

        fields.refreshPlot(defaultValues.getTemperatureField(), defaultValues);

        frame.pack();

        frame.setVisible(true);
    }

    private void setRun(boolean run) {
        this.run = run;
        if (this.run) {
            this.runButton.setText("Stop");
            this.timer.start();
        } else {
            this.runButton.setText("Run");
            this.timer.stop();
        }
    }

    private void setDefaultValues() {
        textField1.setValue(Default.BODY_START_TEMPERATURE);
        textField2.setValue(Default.FLOW_START_TEMPERATURE);
        textField3.setValue(Default.FLOW_END_TEMPERATURE);
        textField4.setValue(Default.TEMPERATURE_INCREASE);
        textField5.setValue(Default.INNER_RADIUS);
        textField6.setValue(Default.WALL_LENGTH);
        textField7.setValue(Default.INNER_FILM_KOEFFICIENT);
        textField8.setValue(Default.OUTER_FILM_KOEFFICIENT);
        textField9.setValue(Default.HEAT_CONDUCTIVITY);
        textField10.setValue(Default.HEAT_CAPACITY);
        textField11.setValue(Default.NODES);
        textField12.setValue(Default.TIME_UNIT);
    }

    private void refreshPlot(Computer computer) {
        refreshPlot(computer.computeTemperatureField(), computer.getInnerRadius(), computer.getRadiusStep(), computer.getTimePassed());
    }

    private void refreshPlot(double[] specificOperationResult, Computer computer) {
        refreshPlot(specificOperationResult, computer.getInnerRadius(), computer.getRadiusStep(), computer.getTimePassed());
    }

    private void refreshPlot(double[] temperatureField, double radius, double radiusStep, double time) {
        XYSeriesCollection ss = (XYSeriesCollection) this.chartPanel1.getChart().getXYPlot().getDataset();
        XYSeries s = ss.getSeriesCount() == 0 ? null : ss.getSeries(0);
        String key = "Temperature field after " + time + " seconds";
        if (temperatureField == null) {
            String warn = "Temperature field can't be null";
            JOptionPane.showMessageDialog(this.tabbedPane1.getParent(), warn, "Error", JOptionPane.WARNING_MESSAGE);
            throw new IllegalStateException(warn);
        }

        if (s == null) {
            s = new XYSeries(key, false, false);
            ss.addSeries(s);
        } else {
            s.clear();
            s.setKey(key);
        }

        for (int i = 0; i < temperatureField.length; ++i) {
            s.addOrUpdate(radius + radiusStep * i, temperatureField[i]);
        }

    }

    private void createUIComponents() {
        chartPanel1 = new ChartPanel(null);
        chartPanel1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        chartPanel1.setBackground(Color.WHITE);
        chartPanel1.setChart(ChartFactory.createXYLineChart(
                "Temperature field",
                "radius, m",
                "temperature, °C",
                new XYSeriesCollection(),
                PlotOrientation.VERTICAL,
                true,
                false,
                false
        ));
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        panel1.setMaximumSize(new Dimension(247, 168));
        panel1.setMinimumSize(new Dimension(247, 168));
        panel1.setOpaque(false);
        panel1.setPreferredSize(new Dimension(247, 168));
        tabbedPane1 = new JTabbedPane();
        panel1.add(tabbedPane1, BorderLayout.CENTER);
        propsPanel = new JPanel();
        propsPanel.setLayout(new GridBagLayout());
        propsPanel.setMaximumSize(new Dimension(247, 168));
        tabbedPane1.addTab("Props", propsPanel);
        textPanel = new JPanel();
        textPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 9.0;
        gbc.fill = GridBagConstraints.BOTH;
        propsPanel.add(textPanel, gbc);
        textPanel1 = new JPanel();
        textPanel1.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        textPanel.add(textPanel1, gbc);
        textField1 = new JFormattedTextField();
        textField1.setMaximumSize(new Dimension(49, 24));
        textField1.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 9.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        textPanel1.add(textField1, gbc);
        textLable1 = new JLabel();
        textLable1.setHorizontalAlignment(0);
        textLable1.setHorizontalTextPosition(10);
        textLable1.setText("T0");
        textLable1.setVerticalAlignment(0);
        textLable1.setVerticalTextPosition(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        textPanel1.add(textLable1, gbc);
        textPanel2 = new JPanel();
        textPanel2.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        textPanel.add(textPanel2, gbc);
        textField2 = new JFormattedTextField();
        textField2.setMaximumSize(new Dimension(49, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 9.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        textPanel2.add(textField2, gbc);
        textLable2 = new JLabel();
        textLable2.setHorizontalAlignment(0);
        textLable2.setHorizontalTextPosition(10);
        textLable2.setText("T1");
        textLable2.setVerticalAlignment(0);
        textLable2.setVerticalTextPosition(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        textPanel2.add(textLable2, gbc);
        textPanel3 = new JPanel();
        textPanel3.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        textPanel.add(textPanel3, gbc);
        textField3 = new JFormattedTextField();
        textField3.setMaximumSize(new Dimension(49, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 9.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        textPanel3.add(textField3, gbc);
        textLable3 = new JLabel();
        textLable3.setHorizontalAlignment(0);
        textLable3.setHorizontalTextPosition(10);
        textLable3.setText("T2");
        textLable3.setVerticalAlignment(0);
        textLable3.setVerticalTextPosition(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        textPanel3.add(textLable3, gbc);
        textPanel4 = new JPanel();
        textPanel4.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        textPanel.add(textPanel4, gbc);
        textField4 = new JFormattedTextField();
        textField4.setMaximumSize(new Dimension(49, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 9.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        textPanel4.add(textField4, gbc);
        textLable4 = new JLabel();
        textLable4.setHorizontalAlignment(0);
        textLable4.setHorizontalTextPosition(10);
        textLable4.setMaximumSize(new Dimension(13, 16));
        textLable4.setMinimumSize(new Dimension(13, 16));
        textLable4.setPreferredSize(new Dimension(13, 16));
        textLable4.setText("Vt");
        textLable4.setVerticalAlignment(0);
        textLable4.setVerticalTextPosition(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        textPanel4.add(textLable4, gbc);
        textPanel5 = new JPanel();
        textPanel5.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        textPanel.add(textPanel5, gbc);
        textField5 = new JFormattedTextField();
        textField5.setMaximumSize(new Dimension(49, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 9.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        textPanel5.add(textField5, gbc);
        textLable5 = new JLabel();
        textLable5.setHorizontalAlignment(0);
        textLable5.setHorizontalTextPosition(10);
        textLable5.setMaximumSize(new Dimension(13, 16));
        textLable5.setMinimumSize(new Dimension(13, 16));
        textLable5.setPreferredSize(new Dimension(13, 16));
        textLable5.setText("r1");
        textLable5.setVerticalAlignment(0);
        textLable5.setVerticalTextPosition(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        textPanel5.add(textLable5, gbc);
        textPanel6 = new JPanel();
        textPanel6.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        textPanel.add(textPanel6, gbc);
        textField6 = new JFormattedTextField();
        textField6.setMaximumSize(new Dimension(49, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 9.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        textPanel6.add(textField6, gbc);
        textLable6 = new JLabel();
        textLable6.setHorizontalAlignment(0);
        textLable6.setHorizontalTextPosition(10);
        textLable6.setMaximumSize(new Dimension(13, 16));
        textLable6.setMinimumSize(new Dimension(13, 16));
        textLable6.setPreferredSize(new Dimension(13, 16));
        textLable6.setText("sb");
        textLable6.setVerticalAlignment(0);
        textLable6.setVerticalTextPosition(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        textPanel6.add(textLable6, gbc);
        textPanel7 = new JPanel();
        textPanel7.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        textPanel.add(textPanel7, gbc);
        textField7 = new JFormattedTextField();
        textField7.setMaximumSize(new Dimension(49, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 9.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        textPanel7.add(textField7, gbc);
        textLable7 = new JLabel();
        textLable7.setHorizontalAlignment(0);
        textLable7.setHorizontalTextPosition(10);
        textLable7.setText("α1");
        textLable7.setVerticalAlignment(0);
        textLable7.setVerticalTextPosition(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        textPanel7.add(textLable7, gbc);
        textPanel8 = new JPanel();
        textPanel8.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        textPanel.add(textPanel8, gbc);
        textField8 = new JFormattedTextField();
        textField8.setMaximumSize(new Dimension(49, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 9.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        textPanel8.add(textField8, gbc);
        textLable8 = new JLabel();
        textLable8.setHorizontalAlignment(0);
        textLable8.setHorizontalTextPosition(10);
        textLable8.setText("α2");
        textLable8.setVerticalAlignment(0);
        textLable8.setVerticalTextPosition(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        textPanel8.add(textLable8, gbc);
        textPanel9 = new JPanel();
        textPanel9.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        textPanel.add(textPanel9, gbc);
        textField9 = new JFormattedTextField();
        textField9.setMaximumSize(new Dimension(49, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 9.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        textPanel9.add(textField9, gbc);
        textLable9 = new JLabel();
        textLable9.setHorizontalAlignment(0);
        textLable9.setHorizontalTextPosition(10);
        textLable9.setMaximumSize(new Dimension(13, 16));
        textLable9.setMinimumSize(new Dimension(13, 16));
        textLable9.setPreferredSize(new Dimension(13, 16));
        textLable9.setText("λ");
        textLable9.setVerticalAlignment(0);
        textLable9.setVerticalTextPosition(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        textPanel9.add(textLable9, gbc);
        textPanel10 = new JPanel();
        textPanel10.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        textPanel.add(textPanel10, gbc);
        textField10 = new JFormattedTextField();
        textField10.setMaximumSize(new Dimension(49, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 9.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        textPanel10.add(textField10, gbc);
        textLable10 = new JLabel();
        textLable10.setHorizontalAlignment(0);
        textLable10.setHorizontalTextPosition(10);
        textLable10.setOpaque(false);
        textLable10.setText("cp");
        textLable10.setVerticalAlignment(0);
        textLable10.setVerticalTextPosition(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        textPanel10.add(textLable10, gbc);
        textPanel11 = new JPanel();
        textPanel11.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        textPanel.add(textPanel11, gbc);
        textField11 = new JFormattedTextField();
        textField11.setMaximumSize(new Dimension(49, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 9.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        textPanel11.add(textField11, gbc);
        textLable11 = new JLabel();
        textLable11.setHorizontalAlignment(0);
        textLable11.setHorizontalTextPosition(10);
        textLable11.setMaximumSize(new Dimension(13, 16));
        textLable11.setMinimumSize(new Dimension(13, 16));
        textLable11.setPreferredSize(new Dimension(13, 16));
        textLable11.setText("nodes");
        textLable11.setVerticalAlignment(0);
        textLable11.setVerticalTextPosition(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        textPanel11.add(textLable11, gbc);
        textPanel12 = new JPanel();
        textPanel12.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        textPanel.add(textPanel12, gbc);
        textField12 = new JFormattedTextField();
        textField12.setMaximumSize(new Dimension(49, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 9.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        textPanel12.add(textField12, gbc);
        textLable12 = new JLabel();
        textLable12.setHorizontalAlignment(0);
        textLable12.setHorizontalTextPosition(10);
        textLable12.setMaximumSize(new Dimension(13, 16));
        textLable12.setMinimumSize(new Dimension(13, 16));
        textLable12.setPreferredSize(new Dimension(13, 16));
        textLable12.setText("Δτ");
        textLable12.setVerticalAlignment(0);
        textLable12.setVerticalTextPosition(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        textPanel12.add(textLable12, gbc);
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridBagLayout());
        buttonsPanel.setEnabled(true);
        buttonsPanel.setMaximumSize(new Dimension(247, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        propsPanel.add(buttonsPanel, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 8.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonsPanel.add(spacer1, gbc);
        acceptButton = new JButton();
        acceptButton.setPreferredSize(new Dimension(79, 24));
        acceptButton.setText("Calculate");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 40, 0, 0);
        buttonsPanel.add(acceptButton, gbc);
        resetButton = new JButton();
        resetButton.setMaximumSize(new Dimension(79, 24));
        resetButton.setMinimumSize(new Dimension(79, 24));
        resetButton.setPreferredSize(new Dimension(79, 24));
        resetButton.setText("Reset");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 40);
        buttonsPanel.add(resetButton, gbc);
        graphPanel = new JPanel();
        graphPanel.setLayout(new GridBagLayout());
        graphPanel.setMaximumSize(new Dimension(600, 700));
        graphPanel.setMinimumSize(new Dimension(600, 700));
        graphPanel.setOpaque(true);
        graphPanel.setPreferredSize(new Dimension(600, 700));
        tabbedPane1.addTab("Graph", graphPanel);
        drawingPanel = new JPanel();
        drawingPanel.setLayout(new BorderLayout(0, 0));
        drawingPanel.setMinimumSize(new Dimension(124, 144));
        drawingPanel.setPreferredSize(new Dimension(124, 144));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 9.0;
        gbc.fill = GridBagConstraints.BOTH;
        graphPanel.add(drawingPanel, gbc);
        drawingPanel.add(chartPanel1, BorderLayout.CENTER);
        buttonsPanel2 = new JPanel();
        buttonsPanel2.setLayout(new GridBagLayout());
        buttonsPanel2.setEnabled(true);
        buttonsPanel2.setMaximumSize(new Dimension(247, 24));
        buttonsPanel2.setMinimumSize(new Dimension(247, 24));
        buttonsPanel2.setPreferredSize(new Dimension(247, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        graphPanel.add(buttonsPanel2, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 4.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonsPanel2.add(spacer2, gbc);
        runButton = new JButton();
        runButton.setText("Run");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 40, 0, 0);
        buttonsPanel2.add(runButton, gbc);
        timeSlider = new JSlider();
        timeSlider.setMaximum(7);
        timeSlider.setMaximumSize(new Dimension(2147483647, 2147483647));
        timeSlider.setMinimum(1);
        timeSlider.setMinimumSize(new Dimension(79, 24));
        timeSlider.setPreferredSize(new Dimension(79, 24));
        timeSlider.setValue(4);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 4.0;
        gbc.weighty = 5.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 40);
        buttonsPanel2.add(timeSlider, gbc);
        restartButton = new JButton();
        restartButton.setText("Restart");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 20, 0, 20);
        buttonsPanel2.add(restartButton, gbc);
        speedText = new JLabel();
        Font speedTextFont = this.$$$getFont$$$(null, -1, 14, speedText.getFont());
        if (speedTextFont != null) speedText.setFont(speedTextFont);
        speedText.setText("x1");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 4.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 40);
        buttonsPanel2.add(speedText, gbc);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}
