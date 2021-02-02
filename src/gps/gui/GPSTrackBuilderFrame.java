/*     */ package gps.gui;
/*     */ 
/*     */

import gps.track.GPSTrack;
import gps.track.GPSTrackPoint;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GPSTrackBuilderFrame
/*     */   extends JFrame
/*     */ {
/*     */   private static final long serialVersionUID = 2698799947780583769L;
    /*  45 */
    /*     */
    /*  47 */
    /*     */
/*     */   private JTextField txtSrcFileName;
/*     */   
/*     */   private JButton btnChooseSrcFile;
/*     */   
/*     */   private JTextField txtTrackName;
/*     */   
/*     */   private JSpinner spinHeight;
/*     */   
/*     */   private JTextField txtHeightDeviation;
/*     */   
/*     */   private JSpinner spinPointsPerSection;
/*     */   
/*     */   private JTextField txtDesc;
/*     */   private JTextField txtRev1FileName;
/*     */   private JButton btnChooseRev1File;
/*     */   private JSpinner spinRev1DateFrom;
/*     */   private JSpinner spinRev1DateTo;
/*     */   private JTextField txtMaxRev1Deviation;
/*     */   private JTextField txtRev2FileName;
/*     */   private JButton btnChooseRev2File;
/*     */   private JSpinner spinRev2DateFrom;
/*     */   private JSpinner spinRev2DateTo;
/*     */   private JTextField txtMaxRev2Deviation;
/*     */   @SuppressWarnings("FieldCanBeLocal")
private JButton btnGenerateAll;
/*     */   
/*     */   public GPSTrackBuilderFrame() {
/*  80 */     setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
/*  81 */     setSize(700, 700);
/*  82 */     setTitle("Преобразователь GPS-трека");
/*  83 */     setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
/*     */     
/*  85 */     selectLookAndFeel();
/*     */     
/*  87 */     initControls();
/*     */     
/*  89 */     localizeFileChooserToRussian();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JPanel createSourceDataPanel() {
/*  97 */     JPanel pnlSourceData = new JPanel(new GridBagLayout());
/*  98 */     GridBagConstraints gbc = new GridBagConstraints();
/*  99 */     pnlSourceData.setBorder(new TitledBorder("Исходные данные"));
/*     */     
/* 101 */     this.txtSrcFileName = new JTextField(getCurrentDir() + "86.1.gpx");
/* 102 */     this.btnChooseSrcFile = new JButton();
/* 103 */     this.txtTrackName = new JTextField("Трек");
/* 104 */     this.txtDesc = new JTextField("Описание");
/* 105 */     this.spinHeight = new JSpinner(new SpinnerNumberModel(250.0D, -500.0D, 500.0D, 0.1D));
/* 106 */     this.txtHeightDeviation = new JTextField("1.0");
/* 107 */     this.spinPointsPerSection = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
/*     */     
/* 109 */     gbc.fill = 2;
/* 110 */     gbc.gridx = 0;
/* 111 */     gbc.gridy = -1;
/* 112 */     gbc.weightx = 0.1D;
/* 113 */     pnlSourceData.add(new JLabel("Имя исходного файла (.gpx)"), gbc);
/* 114 */     pnlSourceData.add(new JLabel("Наименование трека"), gbc);
/* 115 */     pnlSourceData.add(new JLabel("Высота над уровнем моря"), gbc);
/* 116 */     pnlSourceData.add(new JLabel("Отклонение высоты"), gbc);
/* 117 */     pnlSourceData.add(new JLabel("Число промежуточных точек"), gbc);
/* 118 */     pnlSourceData.add(new JLabel("Описание трека"), gbc);
/* 119 */     gbc.gridx = 1;
/* 120 */     gbc.weightx = 2.0D;
/* 121 */     pnlSourceData.add(makeChooseFileBox(this.txtSrcFileName, this.btnChooseSrcFile), gbc);
/* 122 */     pnlSourceData.add(this.txtTrackName, gbc);
/* 123 */     pnlSourceData.add(this.spinHeight, gbc);
/* 124 */     pnlSourceData.add(this.txtHeightDeviation, gbc);
/* 125 */     pnlSourceData.add(this.spinPointsPerSection, gbc);
/* 126 */     pnlSourceData.add(this.txtDesc, gbc);
/* 127 */     return pnlSourceData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JPanel createRev1DataPanel() {
/* 135 */     Calendar cal = Calendar.getInstance();
/* 136 */     cal.setTime(new Date());
/*     */     
/* 138 */     JPanel pnlRev1Data = new JPanel(new GridBagLayout());
/* 139 */     GridBagConstraints gbc = new GridBagConstraints();
/* 140 */     pnlRev1Data.setBorder(new TitledBorder("Данные затирки"));
/*     */ 
/*     */     
/* 143 */     this.txtRev1FileName = new JTextField(System.getProperty("user.dir") + File.separator + "rev1.gpx");
/* 144 */     this.btnChooseRev1File = new JButton();
/*     */     
/* 146 */     this.spinRev1DateFrom = new JSpinner(new SpinnerDateModel());
/* 147 */     this.spinRev1DateFrom.setEditor(new JSpinner.DateEditor(this.spinRev1DateFrom, "dd MMMM yyyy   HH:mm:ss"));
/* 148 */     this.spinRev1DateFrom.setValue(cal.getTime());
/*     */     
/* 150 */     this.spinRev1DateTo = new JSpinner(new SpinnerDateModel());
/* 151 */     this.spinRev1DateTo.setEditor(new JSpinner.DateEditor(this.spinRev1DateTo, "dd MMMM yyyy   HH:mm:ss"));
/* 152 */     cal.add(Calendar.HOUR, 3);
/* 153 */     this.spinRev1DateTo.setValue(cal.getTime());
/*     */     
/* 155 */     this.txtMaxRev1Deviation = new JTextField("0.0005");
/*     */     
/* 157 */     gbc.fill = 2;
/* 158 */     gbc.gridx = 0;
/* 159 */     gbc.gridy = -1;
/* 160 */     gbc.weightx = 0.1D;
/* 161 */     pnlRev1Data.add(new JLabel("Имя выходного файла (.gpx)"), gbc);
/* 162 */     pnlRev1Data.add(new JLabel("Дата и время начала"), gbc);
/* 163 */     pnlRev1Data.add(new JLabel("Дата и время окончания"), gbc);
/* 164 */     pnlRev1Data.add(new JLabel("Отклонение координат"), gbc);
/* 165 */     gbc.gridx = 1;
/* 166 */     gbc.weightx = 2.0D;
/* 167 */     pnlRev1Data.add(makeChooseFileBox(this.txtRev1FileName, this.btnChooseRev1File), gbc);
/* 168 */     pnlRev1Data.add(this.spinRev1DateFrom, gbc);
/* 169 */     pnlRev1Data.add(this.spinRev1DateTo, gbc);
/* 170 */     pnlRev1Data.add(this.txtMaxRev1Deviation, gbc);
/* 171 */     return pnlRev1Data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JPanel createRev2DataPanel() {
/* 179 */     Calendar cal = Calendar.getInstance();
/* 180 */     cal.setTime(new Date());
/*     */     
/* 182 */     JPanel pnlRev2Data = new JPanel(new GridBagLayout());
/* 183 */     GridBagConstraints gbc = new GridBagConstraints();
/* 184 */     pnlRev2Data.setBorder(new TitledBorder("Данные учета"));
/*     */ 
/*     */     
/* 187 */     this.txtRev2FileName = new JTextField(System.getProperty("user.dir") + File.separator + "rev2.gpx");
/* 188 */     this.btnChooseRev2File = new JButton();
/*     */     
/* 190 */     this.spinRev2DateFrom = new JSpinner(new SpinnerDateModel());
/* 191 */     this.spinRev2DateFrom.setEditor(new JSpinner.DateEditor(this.spinRev2DateFrom, "dd MMMM yyyy   HH:mm:ss"));
/* 192 */     cal.add(Calendar.DATE, 1);
/* 193 */     cal.add(Calendar.HOUR, 3);
/* 194 */     this.spinRev2DateFrom.setValue(cal.getTime());
/*     */     
/* 196 */     this.spinRev2DateTo = new JSpinner(new SpinnerDateModel());
/* 197 */     this.spinRev2DateTo.setEditor(new JSpinner.DateEditor(this.spinRev2DateTo, "dd MMMM yyyy   HH:mm:ss"));
/* 198 */     cal.add(Calendar.HOUR, 3);
/* 199 */     this.spinRev2DateTo.setValue(cal.getTime());
/*     */     
/* 201 */     this.txtMaxRev2Deviation = new JTextField("0.00005");
/*     */     
/* 203 */     gbc.fill = 2;
/* 204 */     gbc.gridx = 0;
/* 205 */     gbc.gridy = -1;
/* 206 */     gbc.weightx = 0.1D;
/* 207 */     pnlRev2Data.add(new JLabel("Имя выходного файла (.gpx)"), gbc);
/* 208 */     pnlRev2Data.add(new JLabel("Дата и время начала"), gbc);
/* 209 */     pnlRev2Data.add(new JLabel("Дата и время окончания"), gbc);
/* 210 */     pnlRev2Data.add(new JLabel("Отклонение координат"), gbc);
/* 211 */     gbc.gridx = 1;
/* 212 */     gbc.weightx = 2.0D;
/* 213 */     pnlRev2Data.add(makeChooseFileBox(this.txtRev2FileName, this.btnChooseRev2File), gbc);
/* 214 */     pnlRev2Data.add(this.spinRev2DateFrom, gbc);
/* 215 */     pnlRev2Data.add(this.spinRev2DateTo, gbc);
/* 216 */     pnlRev2Data.add(this.txtMaxRev2Deviation, gbc);
/* 217 */     return pnlRev2Data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initControls() {
/* 225 */     JPanel pnlSourceData = createSourceDataPanel();
/*     */     
/* 227 */     JPanel pnlRev1Data = createRev1DataPanel();
/*     */     
/* 229 */     JPanel pnlRev2Data = createRev2DataPanel();
/*     */     
/* 231 */     this.btnGenerateAll = new JButton("Создать все треки");
/* 232 */     this.btnGenerateAll.addActionListener(new GenerateButtonListener());
/* 233 */     Box boxBottom = Box.createHorizontalBox();
/* 234 */     boxBottom.add(Box.createHorizontalGlue());
/* 235 */     boxBottom.add(this.btnGenerateAll);
/* 236 */     boxBottom.add(Box.createRigidArea(new Dimension(15, 40)));
/*     */     
/* 238 */     add(pnlSourceData);
/* 239 */     add(pnlRev1Data);
/* 240 */     add(pnlRev2Data);
/* 241 */     add(boxBottom);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/* 248 */     /*     *//*     *//*     *//*     *//*     */
    EventQueue.invokeLater(() -> {
    /* 253 */             GPSTrackBuilderFrame frame = new GPSTrackBuilderFrame();
    /* 254 */             frame.setVisible(true);
    /*     */           });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getCurrentDir() {
/* 263 */     return System.getProperty("user.dir") + File.separator;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Box makeChooseFileBox(JTextField textField, JButton button) {
/* 269 */     Box box = Box.createHorizontalBox();
/* 270 */     box.add(textField);
/* 271 */     box.add(button);
/*     */     try {
/* 273 */       Image img = ImageIO.read(getClass().getResource("/images/folder.png"));
/* 274 */       button.setIcon(new ImageIcon(img));
/*     */     }
/* 276 */     catch (IOException e) {
/*     */       
/* 278 */       e.printStackTrace();
/*     */     } 
/* 280 */     button.addActionListener(new ChooseFileButtonListener());
/* 281 */     return box;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Date getDateFromControl(JSpinner spinner) {
/* 288 */     return (Date)spinner.getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void selectLookAndFeel() {
/* 295 */     String lafName = UIManager.getSystemLookAndFeelClassName();
/* 296 */     UIManager.LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels(); byte b; int i; UIManager.LookAndFeelInfo[] arrayOfLookAndFeelInfo1;
/* 297 */     for (i = (arrayOfLookAndFeelInfo1 = lafs).length, b = 0; b < i; ) { UIManager.LookAndFeelInfo l = arrayOfLookAndFeelInfo1[b];
/* 298 */       if (l.getClassName().contains("Nimbus")) {
/*     */         
/* 300 */         lafName = l.getClassName(); break;
/*     */       } 
/*     */       b++; }
/*     */     
/*     */     try {
/* 305 */       UIManager.setLookAndFeel(lafName);
/*     */     
/*     */     }
/* 308 */     catch (ClassNotFoundException|InstantiationException|IllegalAccessException|javax.swing.UnsupportedLookAndFeelException e) {
/*     */       
/* 310 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void localizeFileChooserToRussian() {
/* 318 */     UIManager.put("FileChooser.fileNameLabelText", "Имя файла:");
/* 319 */     UIManager.put("FileChooser.lookInLabelText", "Смотреть в");
/* 320 */     UIManager.put("FileChooser.filesOfTypeLabelText", "Типы файлов:");
/* 321 */     UIManager.put("FileChooser.upFolderToolTipText", "На один уровень вверх");
/* 322 */     UIManager.put("FileChooser.homeFolderToolTipText", "Домой");
/* 323 */     UIManager.put("FileChooser.newFolderToolTipText", "Создать новую папку");
/* 324 */     UIManager.put("FileChooser.listViewButtonToolTipText", "Список");
/* 325 */     UIManager.put("FileChooser.detailsViewButtonToolTipText", "Подробно");
/* 326 */     UIManager.put("FileChooser.fileNameHeaderText", "Имя");
/* 327 */     UIManager.put("FileChooser.fileSizeHeaderText", "Размер");
/* 328 */     UIManager.put("FileChooser.fileTypeHeaderText", "Тип");
/* 329 */     UIManager.put("FileChooser.fileDateHeaderText", "Изменен");
/* 330 */     UIManager.put("FileChooser.fileAttrHeaderText", "Атрибуты");
/* 331 */     UIManager.put("FileChooser.fileSizeKiloBytes", "{0} Кб");
/* 332 */     UIManager.put("FileChooser.fileSizeMegaBytes", "{0} Мб");
/* 333 */     UIManager.put("FileChooser.fileSizeGigaBytes", "{0} Гб");
/* 334 */     UIManager.put("FileChooser.viewMenuLabelText", "Настроить вид");
/* 335 */     UIManager.put("FileChooser.listViewActionLabelText", "Список");
/* 336 */     UIManager.put("FileChooser.detailsViewActionLabelText", "Подробно");
/* 337 */     UIManager.put("FileChooser.refreshActionLabelText", "Обновить");
/* 338 */     UIManager.put("FileChooser.newFolderActionLabelText", "Новая папка");
/* 339 */     UIManager.put("FileChooser.acceptAllFileFilterText", "Все файлы");
/* 340 */     UIManager.put("FileChooser.saveButtonText", "Сохранить");
/* 341 */     UIManager.put("FileChooser.openButtonText", "Открыть");
/* 342 */     UIManager.put("FileChooser.openButtonText", "Открыть");
/* 343 */     UIManager.put("FileChooser.cancelButtonText", "Отмена");
/* 344 */     UIManager.put("FileChooser.updateButtonText", "Обновить");
/* 345 */     UIManager.put("FileChooser.helpButtonText", "Помощь");
/* 346 */     UIManager.put("FileChooser.saveButtonToolTipText", "Сохранить");
/* 347 */     UIManager.put("FileChooser.openButtonToolTipText", "Открыть");
/* 348 */     UIManager.put("FileChooser.cancelButtonToolTipText", "Отмена");
/* 349 */     UIManager.put("FileChooser.updateButtonToolTipText", "Обновить");
/* 350 */     UIManager.put("FileChooser.helpButtonToolTipText", "Помощь");
/* 351 */     UIManager.put("FileChooser.openDialogTitleText", "Выбрать исходный файл");
/* 352 */     UIManager.put("FileChooser.saveDialogTitleText", "Выбрать выходной файл");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean checkAllFilePaths() {
/* 359 */     boolean result = true;
/* 360 */     String errMsg = "";
/* 361 */     if (!checkSourceFileExists(this.txtSrcFileName.getText())) {
/*     */       
/* 363 */       result = false;
/* 364 */       errMsg = errMsg + String.format("Неверное имя исходного файла: \n     файл <%s> не существует\n", this.txtSrcFileName.getText());
/*     */     } 
/* 366 */     if (checkOutPathIncorrect(this.txtRev1FileName.getText())) {
/*     */       
/* 368 */       result = false;
/* 369 */       errMsg = errMsg + String.format("Неверный путь к выходному файлу %s: \n     указана несуществующая папка или некорректное имя файла\n", "затирки");
/*     */     } 
/* 371 */     if (checkOutPathIncorrect(this.txtRev2FileName.getText())) {
/*     */       
/* 373 */       result = false;
/* 374 */       errMsg = errMsg + String.format("Неверный путь к выходному файлу %s: \n     указана несуществующая папка или некорректное имя файла\n", "учета");
/*     */     } 
/* 376 */     if (!result)
/*     */     {
/* 378 */       JOptionPane.showMessageDialog(this, errMsg, "Ошибка ввод данных", JOptionPane.ERROR_MESSAGE);
/*     */     }
/* 380 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean checkSourceFileExists(String fileName) {
/* 387 */     File file = new File(fileName);
/* 388 */     return file.isFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean checkOutPathIncorrect(String fileName) {
/* 395 */     File file = new File(fileName);
/* 396 */     File dir = file.getParentFile();
/* 397 */     return (dir == null || !dir.isDirectory() || !fileName.endsWith(".gpx"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class GenerateButtonListener
/*     */     implements ActionListener
/*     */   {
/*     */     public void actionPerformed(ActionEvent e) {
/* 408 */       if (!GPSTrackBuilderFrame.this.checkAllFilePaths()) {
/*     */         return;
/*     */       }
/*     */       
/* 412 */       GPSTrack track = new GPSTrack(
/* 413 */           GPSTrackBuilderFrame.this.txtSrcFileName.getText(), GPSTrackBuilderFrame.this.txtTrackName.getText(), GPSTrackBuilderFrame.this.txtDesc.getText());
/*     */       
/* 415 */       /* 419 */
    List<GPSTrackPoint> rev1Points = track.getTrackWithDeviation(
/* 416 */           GPSTrackBuilderFrame.this.getDateFromControl(GPSTrackBuilderFrame.this.spinRev1DateFrom), 
/* 417 */           GPSTrackBuilderFrame.this.getDateFromControl(GPSTrackBuilderFrame.this.spinRev1DateTo), 
/* 418 */           Double.parseDouble(GPSTrackBuilderFrame.this.txtMaxRev1Deviation.getText()), (Double) GPSTrackBuilderFrame.this.spinHeight.getValue(),
/* 420 */           Double.parseDouble(GPSTrackBuilderFrame.this.txtHeightDeviation.getText()));
/*     */       
/* 422 */       int middleCount = (Integer) GPSTrackBuilderFrame.this.spinPointsPerSection.getValue();
/*     */       
/* 424 */       List<GPSTrackPoint> extRev1Points = GPSTrack.getExtendedTrack(rev1Points, middleCount);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 430 */       /* 434 */
    List<GPSTrackPoint> rev2Points = GPSTrack.createTrackWithDeviation(rev1Points,
/* 431 */           GPSTrackBuilderFrame.this.getDateFromControl(GPSTrackBuilderFrame.this.spinRev2DateFrom), 
/* 432 */           GPSTrackBuilderFrame.this.getDateFromControl(GPSTrackBuilderFrame.this.spinRev2DateTo), 
/* 433 */           Double.parseDouble(GPSTrackBuilderFrame.this.txtMaxRev2Deviation.getText()), (Double) GPSTrackBuilderFrame.this.spinHeight.getValue(),
/* 435 */           Double.parseDouble(GPSTrackBuilderFrame.this.txtHeightDeviation.getText()));
/*     */       
/* 437 */
    assert rev2Points != null;
    List<GPSTrackPoint> extRev2Points = GPSTrack.getExtendedTrack(rev2Points, middleCount);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 443 */       GPSTrack.interpolateHeigts(extRev1Points, extRev2Points);
/*     */       
/* 445 */       track.writeGpxFile(GPSTrackBuilderFrame.this.txtRev1FileName.getText(), extRev1Points);
/*     */       
/* 447 */       track.writeGpxFile(GPSTrackBuilderFrame.this.txtRev2FileName.getText(), extRev2Points);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class ChooseFileButtonListener
/*     */     implements ActionListener
/*     */   {
/*     */     public void actionPerformed(ActionEvent e) {
/* 458 */       if (!(e.getSource() instanceof JButton)) {
/*     */         return;
/*     */       }
/* 461 */       JButton btn = (JButton)e.getSource();
/* 462 */       String fileName;
/*     */       
/* 464 */       JFileChooser fc = new JFileChooser();
/*     */       
/* 466 */       fc.setCurrentDirectory(new File(GPSTrackBuilderFrame.this.getCurrentDir()));
/*     */       
/* 468 */       if (btn == GPSTrackBuilderFrame.this.btnChooseRev1File) {
/* 469 */         fc.setSelectedFile(new File(GPSTrackBuilderFrame.this.getCurrentDir() + File.separator + GPSTrackBuilderFrame.this.txtRev1FileName.getText()));
/* 470 */       } else if (btn == GPSTrackBuilderFrame.this.btnChooseRev2File) {
/* 471 */         fc.setSelectedFile(new File(GPSTrackBuilderFrame.this.getCurrentDir() + File.separator + GPSTrackBuilderFrame.this.txtRev2FileName.getText()));
/*     */       } 
/* 473 */       fc.setFileFilter(new FileNameExtensionFilter("GPS eXchange", "gpx"));
/* 474 */       fc.setAcceptAllFileFilterUsed(false);
/*     */ 
/*     */       
/* 477 */       int retVal = (btn == GPSTrackBuilderFrame.this.btnChooseSrcFile) ? fc.showOpenDialog(btn) : fc.showSaveDialog(btn);
/* 478 */       if (retVal == 0) {
/*     */ 
/*     */         
/* 481 */         fileName = fc.getSelectedFile().getAbsolutePath();
/* 482 */         if (!fileName.endsWith(".gpx"))
/* 483 */           fileName = fileName + ".gpx";
/* 484 */         if (btn == GPSTrackBuilderFrame.this.btnChooseSrcFile) {
/* 485 */           GPSTrackBuilderFrame.this.txtSrcFileName.setText(fileName);
/* 486 */         } else if (btn == GPSTrackBuilderFrame.this.btnChooseRev1File) {
/* 487 */           GPSTrackBuilderFrame.this.txtRev1FileName.setText(fileName);
/*     */         } else {
/* 489 */           GPSTrackBuilderFrame.this.txtRev2FileName.setText(fileName);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\dev\TZ\GPSTrackBuilder.jar!\gps\gui\GPSTrackBuilderFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */