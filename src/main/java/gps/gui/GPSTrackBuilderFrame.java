package gps.gui;


import gps.track.Builder;
import gps.track.BuilderArgs;
import gps.track.BuilderOutArgs;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class GPSTrackBuilderFrame
        extends JFrame {
    private static final long serialVersionUID = 2698799947780583769L;


    private JTextField txtSrcFileName;

    private JButton btnChooseSrcFile;

    private JTextField txtTrackName;

    private JSpinner spinPointsPerSection;

    private JTextField txtDesc;
    private JTextField txtRev1FileName;
    private JButton btnChooseRev1File;
    private JSpinner spinRev1DateFrom;
    private JSpinner spinRev1DateTo;
    private JTextField txtMaxRev1Deviation;
    private JTextField txtRev2FileName;
    private JButton btnChooseRev2File;
    private JSpinner spinRev2DateFrom;
    private JSpinner spinRev2DateTo;
    private JTextField txtMaxRev2Deviation;
    @SuppressWarnings("FieldCanBeLocal")
    private JButton btnGenerateAll;

    public GPSTrackBuilderFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(700, 700);
        setTitle("Преобразователь GPS-трека");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        selectLookAndFeel();

        initControls();

        localizeFileChooserToRussian();
    }

    private JPanel createSourceDataPanel() {
        JPanel pnlSourceData = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        pnlSourceData.setBorder(new TitledBorder("Исходные данные"));

        this.txtSrcFileName = new JTextField(getCurrentDir() + "86.1.gpx");
        this.btnChooseSrcFile = new JButton();
        this.txtTrackName = new JTextField("Трек");
        this.txtDesc = new JTextField("Описание");
        this.spinPointsPerSection = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));

        gbc.fill = 2;
        gbc.gridx = 0;
        gbc.gridy = -1;
        gbc.weightx = 0.1D;
        pnlSourceData.add(new JLabel("Имя исходного файла (.gpx)"), gbc);
        pnlSourceData.add(new JLabel("Наименование трека"), gbc);
        pnlSourceData.add(new JLabel("Число промежуточных точек"), gbc);
        pnlSourceData.add(new JLabel("Описание трека"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 2.0D;
        pnlSourceData.add(makeChooseFileBox(this.txtSrcFileName, this.btnChooseSrcFile), gbc);
        pnlSourceData.add(this.txtTrackName, gbc);
        pnlSourceData.add(this.spinPointsPerSection, gbc);
        pnlSourceData.add(this.txtDesc, gbc);
        return pnlSourceData;
    }


    private JPanel createRev1DataPanel() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        JPanel pnlRev1Data = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        pnlRev1Data.setBorder(new TitledBorder("Данные затирки"));


        this.txtRev1FileName = new JTextField(System.getProperty("user.dir") + File.separator + "rev1.gpx");
        this.btnChooseRev1File = new JButton();

        this.spinRev1DateFrom = new JSpinner(new SpinnerDateModel());
        this.spinRev1DateFrom.setEditor(new JSpinner.DateEditor(this.spinRev1DateFrom, "dd MMMM yyyy   HH:mm:ss"));
        this.spinRev1DateFrom.setValue(cal.getTime());

        this.spinRev1DateTo = new JSpinner(new SpinnerDateModel());
        this.spinRev1DateTo.setEditor(new JSpinner.DateEditor(this.spinRev1DateTo, "dd MMMM yyyy   HH:mm:ss"));
        cal.add(Calendar.HOUR, 3);
        this.spinRev1DateTo.setValue(cal.getTime());

        this.txtMaxRev1Deviation = new JTextField("0.0005");

        gbc.fill = 2;
        gbc.gridx = 0;
        gbc.gridy = -1;
        gbc.weightx = 0.1D;
        pnlRev1Data.add(new JLabel("Имя выходного файла (.gpx)"), gbc);
        pnlRev1Data.add(new JLabel("Дата и время начала"), gbc);
        pnlRev1Data.add(new JLabel("Дата и время окончания"), gbc);
        pnlRev1Data.add(new JLabel("Отклонение координат"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 2.0D;
        pnlRev1Data.add(makeChooseFileBox(this.txtRev1FileName, this.btnChooseRev1File), gbc);
        pnlRev1Data.add(this.spinRev1DateFrom, gbc);
        pnlRev1Data.add(this.spinRev1DateTo, gbc);
        pnlRev1Data.add(this.txtMaxRev1Deviation, gbc);
        return pnlRev1Data;
    }


    private JPanel createRev2DataPanel() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        JPanel pnlRev2Data = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        pnlRev2Data.setBorder(new TitledBorder("Данные учета"));


        this.txtRev2FileName = new JTextField(System.getProperty("user.dir") + File.separator + "rev2.gpx");
        this.btnChooseRev2File = new JButton();

        this.spinRev2DateFrom = new JSpinner(new SpinnerDateModel());
        this.spinRev2DateFrom.setEditor(new JSpinner.DateEditor(this.spinRev2DateFrom, "dd MMMM yyyy   HH:mm:ss"));
        cal.add(Calendar.DATE, 1);
        cal.add(Calendar.HOUR, 3);
        this.spinRev2DateFrom.setValue(cal.getTime());

        this.spinRev2DateTo = new JSpinner(new SpinnerDateModel());
        this.spinRev2DateTo.setEditor(new JSpinner.DateEditor(this.spinRev2DateTo, "dd MMMM yyyy   HH:mm:ss"));
        cal.add(Calendar.HOUR, 3);
        this.spinRev2DateTo.setValue(cal.getTime());

        this.txtMaxRev2Deviation = new JTextField("0.00005");

        gbc.fill = 2;
        gbc.gridx = 0;
        gbc.gridy = -1;
        gbc.weightx = 0.1D;
        pnlRev2Data.add(new JLabel("Имя выходного файла (.gpx)"), gbc);
        pnlRev2Data.add(new JLabel("Дата и время начала"), gbc);
        pnlRev2Data.add(new JLabel("Дата и время окончания"), gbc);
        pnlRev2Data.add(new JLabel("Отклонение координат"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 2.0D;
        pnlRev2Data.add(makeChooseFileBox(this.txtRev2FileName, this.btnChooseRev2File), gbc);
        pnlRev2Data.add(this.spinRev2DateFrom, gbc);
        pnlRev2Data.add(this.spinRev2DateTo, gbc);
        pnlRev2Data.add(this.txtMaxRev2Deviation, gbc);
        return pnlRev2Data;
    }


    private void initControls() {
        JPanel pnlSourceData = createSourceDataPanel();

        JPanel pnlRev1Data = createRev1DataPanel();

        JPanel pnlRev2Data = createRev2DataPanel();

        this.btnGenerateAll = new JButton("Создать все треки");
        this.btnGenerateAll.addActionListener(new GenerateButtonListener());
        Box boxBottom = Box.createHorizontalBox();
        boxBottom.add(Box.createHorizontalGlue());
        boxBottom.add(this.btnGenerateAll);
        boxBottom.add(Box.createRigidArea(new Dimension(15, 40)));

        add(pnlSourceData);
        add(pnlRev1Data);
        add(pnlRev2Data);
        add(boxBottom);
    }


    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            GPSTrackBuilderFrame frame = new GPSTrackBuilderFrame();
            frame.setVisible(true);
        });
    }


    private String getCurrentDir() {
        return System.getProperty("user.dir") + File.separator;
    }


    private Box makeChooseFileBox(JTextField textField, JButton button) {
        Box box = Box.createHorizontalBox();
        box.add(textField);
        box.add(button);
        button.setText("Выбрать");
        button.addActionListener(new ChooseFileButtonListener());
        return box;
    }


    private Date getDateFromControl(JSpinner spinner) {
        return (Date) spinner.getValue();
    }


    private void selectLookAndFeel() {
        String lafName = UIManager.getSystemLookAndFeelClassName();
        UIManager.LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
        byte b;
        int i;
        UIManager.LookAndFeelInfo[] arrayOfLookAndFeelInfo1;
        for (i = (arrayOfLookAndFeelInfo1 = lafs).length, b = 0; b < i; ) {
            UIManager.LookAndFeelInfo l = arrayOfLookAndFeelInfo1[b];
            if (l.getClassName().contains("Nimbus")) {

                lafName = l.getClassName();
                break;
            }
            b++;
        }

        try {
            UIManager.setLookAndFeel(lafName);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException e) {

            e.printStackTrace();
        }
    }

    private void localizeFileChooserToRussian() {
        UIManager.put("FileChooser.fileNameLabelText", "Имя файла:");
        UIManager.put("FileChooser.lookInLabelText", "Смотреть в");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Типы файлов:");
        UIManager.put("FileChooser.upFolderToolTipText", "На один уровень вверх");
        UIManager.put("FileChooser.homeFolderToolTipText", "Домой");
        UIManager.put("FileChooser.newFolderToolTipText", "Создать новую папку");
        UIManager.put("FileChooser.listViewButtonToolTipText", "Список");
        UIManager.put("FileChooser.detailsViewButtonToolTipText", "Подробно");
        UIManager.put("FileChooser.fileNameHeaderText", "Имя");
        UIManager.put("FileChooser.fileSizeHeaderText", "Размер");
        UIManager.put("FileChooser.fileTypeHeaderText", "Тип");
        UIManager.put("FileChooser.fileDateHeaderText", "Изменен");
        UIManager.put("FileChooser.fileAttrHeaderText", "Атрибуты");
        UIManager.put("FileChooser.fileSizeKiloBytes", "{0} Кб");
        UIManager.put("FileChooser.fileSizeMegaBytes", "{0} Мб");
        UIManager.put("FileChooser.fileSizeGigaBytes", "{0} Гб");
        UIManager.put("FileChooser.viewMenuLabelText", "Настроить вид");
        UIManager.put("FileChooser.listViewActionLabelText", "Список");
        UIManager.put("FileChooser.detailsViewActionLabelText", "Подробно");
        UIManager.put("FileChooser.refreshActionLabelText", "Обновить");
        UIManager.put("FileChooser.newFolderActionLabelText", "Новая папка");
        UIManager.put("FileChooser.acceptAllFileFilterText", "Все файлы");
        UIManager.put("FileChooser.saveButtonText", "Сохранить");
        UIManager.put("FileChooser.openButtonText", "Открыть");
        UIManager.put("FileChooser.openButtonText", "Открыть");
        UIManager.put("FileChooser.cancelButtonText", "Отмена");
        UIManager.put("FileChooser.updateButtonText", "Обновить");
        UIManager.put("FileChooser.helpButtonText", "Помощь");
        UIManager.put("FileChooser.saveButtonToolTipText", "Сохранить");
        UIManager.put("FileChooser.openButtonToolTipText", "Открыть");
        UIManager.put("FileChooser.cancelButtonToolTipText", "Отмена");
        UIManager.put("FileChooser.updateButtonToolTipText", "Обновить");
        UIManager.put("FileChooser.helpButtonToolTipText", "Помощь");
        UIManager.put("FileChooser.openDialogTitleText", "Выбрать исходный файл");
        UIManager.put("FileChooser.saveDialogTitleText", "Выбрать выходной файл");
    }


    private boolean checkAllFilePaths() {
        boolean result = true;
        String errMsg = "";
        if (!checkSourceFileExists(this.txtSrcFileName.getText())) {

            result = false;
            errMsg = errMsg + String.format("Неверное имя исходного файла: \n     файл <%s> не существует\n", this.txtSrcFileName.getText());
        }
        if (checkOutPathIncorrect(this.txtRev1FileName.getText())) {

            result = false;
            errMsg = errMsg + String.format("Неверный путь к выходному файлу %s: \n     указана несуществующая папка или некорректное имя файла\n", "затирки");
        }
        if (checkOutPathIncorrect(this.txtRev2FileName.getText())) {

            result = false;
            errMsg = errMsg + String.format("Неверный путь к выходному файлу %s: \n     указана несуществующая папка или некорректное имя файла\n", "учета");
        }
        if (!result) {
            JOptionPane.showMessageDialog(this, errMsg, "Ошибка ввод данных", JOptionPane.ERROR_MESSAGE);
        }
        return result;
    }


    private boolean checkSourceFileExists(String fileName) {
        File file = new File(fileName);
        return file.isFile();
    }


    private boolean checkOutPathIncorrect(String fileName) {
        File file = new File(fileName);
        File dir = file.getParentFile();
        return (dir == null || !dir.isDirectory() || !fileName.endsWith(".gpx"));
    }


    class GenerateButtonListener
            implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (!GPSTrackBuilderFrame.this.checkAllFilePaths()) {
                return;
            }

            BuilderArgs args = new BuilderArgs(
                    GPSTrackBuilderFrame.this.txtSrcFileName.getText(),
                    GPSTrackBuilderFrame.this.txtTrackName.getText(),
                    GPSTrackBuilderFrame.this.txtDesc.getText(),
                    (Integer) GPSTrackBuilderFrame.this.spinPointsPerSection.getValue());

            BuilderOutArgs outArgs1 = new BuilderOutArgs(
                    GPSTrackBuilderFrame.this.txtRev1FileName.getText(),
                    GPSTrackBuilderFrame.this.getDateFromControl(GPSTrackBuilderFrame.this.spinRev1DateFrom),
                    GPSTrackBuilderFrame.this.getDateFromControl(GPSTrackBuilderFrame.this.spinRev1DateTo),
                    Double.parseDouble(GPSTrackBuilderFrame.this.txtMaxRev1Deviation.getText()));

            BuilderOutArgs outArgs2 = new BuilderOutArgs(
                    GPSTrackBuilderFrame.this.txtRev2FileName.getText(),
                    GPSTrackBuilderFrame.this.getDateFromControl(GPSTrackBuilderFrame.this.spinRev2DateFrom),
                    GPSTrackBuilderFrame.this.getDateFromControl(GPSTrackBuilderFrame.this.spinRev2DateTo),
                    Double.parseDouble(GPSTrackBuilderFrame.this.txtMaxRev2Deviation.getText()));

            Builder.Perform(args, outArgs1, outArgs2);
        }
    }


    class ChooseFileButtonListener
            implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (!(e.getSource() instanceof JButton)) {
                return;
            }
            JButton btn = (JButton) e.getSource();
            String fileName;

            JFileChooser fc = new JFileChooser();

            fc.setCurrentDirectory(new File(GPSTrackBuilderFrame.this.getCurrentDir()));

            if (btn == GPSTrackBuilderFrame.this.btnChooseRev1File) {
                fc.setSelectedFile(new File(GPSTrackBuilderFrame.this.getCurrentDir() + File.separator + GPSTrackBuilderFrame.this.txtRev1FileName.getText()));
            } else if (btn == GPSTrackBuilderFrame.this.btnChooseRev2File) {
                fc.setSelectedFile(new File(GPSTrackBuilderFrame.this.getCurrentDir() + File.separator + GPSTrackBuilderFrame.this.txtRev2FileName.getText()));
            }
            fc.setFileFilter(new FileNameExtensionFilter("GPS eXchange", "gpx"));
            fc.setAcceptAllFileFilterUsed(false);


            int retVal = (btn == GPSTrackBuilderFrame.this.btnChooseSrcFile) ? fc.showOpenDialog(btn) : fc.showSaveDialog(btn);
            if (retVal == 0) {


                fileName = fc.getSelectedFile().getAbsolutePath();
                if (!fileName.endsWith(".gpx"))
                    fileName = fileName + ".gpx";
                if (btn == GPSTrackBuilderFrame.this.btnChooseSrcFile) {
                    GPSTrackBuilderFrame.this.txtSrcFileName.setText(fileName);
                } else if (btn == GPSTrackBuilderFrame.this.btnChooseRev1File) {
                    GPSTrackBuilderFrame.this.txtRev1FileName.setText(fileName);
                } else {
                    GPSTrackBuilderFrame.this.txtRev2FileName.setText(fileName);
                }
            }
        }
    }
}
