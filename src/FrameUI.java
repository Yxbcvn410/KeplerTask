import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public class FrameUI extends JFrame {
	JTextArea posx_box;
	JTextArea posy_box;
	JTextArea velx_box;
	JTextArea vely_box;
	JTextArea mass_box;
	Canvas canvas;
	int size;
	int accu;
	double step;
	boolean flag = false;
	JButton st_button;
	int MethodID;

	public FrameUI() {
		this.setTitle("Kepler v2.2");
		size = 800;
		accu = 30;
		step = 10;
		MethodID = 2;
		this.setLayout(new GridBagLayout());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridwidth = 2;
		gc.gridheight = 12;
		gc.fill = GridBagConstraints.BOTH;
		gc.insets = new Insets(3, 3, 3, 3);
		gc.weightx = 1;
		gc.weighty = 1;
		gc.gridheight = 1;
		gc.gridwidth = 1;
		gc.anchor = GridBagConstraints.CENTER;

		gc.gridy = 0;
		gc.gridx = 0;
		JLabel posx_label = new JLabel("Position X (x1000 km)");
		this.add(posx_label, gc);

		gc.gridx = 1;
		posx_box = new JTextArea("");
		this.add(posx_box, gc);

		gc.gridy = 1;
		gc.gridx = 0;
		JLabel posy_label = new JLabel("Position Y (x1000 km)");
		this.add(posy_label, gc);

		gc.gridx = 1;
		posy_box = new JTextArea("");
		this.add(posy_box, gc);

		gc.gridy = 2;
		gc.gridx = 0;
		JLabel velx_label = new JLabel("Velocity X (km/s)");
		this.add(velx_label, gc);

		gc.gridx = 1;
		velx_box = new JTextArea("");
		this.add(velx_box, gc);

		gc.gridy = 3;
		gc.gridx = 0;
		JLabel vely_label = new JLabel("Velocity Y (km/s)");
		this.add(vely_label, gc);

		gc.gridx = 1;
		vely_box = new JTextArea("");
		this.add(vely_box, gc);

		gc.gridy = 4;
		gc.gridx = 0;
		JLabel mass_label = new JLabel("Center mass (x10^22 kg)");
		this.add(mass_label, gc);

		gc.gridx = 1;
		mass_box = new JTextArea("");
		this.add(mass_box, gc);

		gc.gridx = 0;
		gc.gridy = 5;
		JButton pr_button = new JButton("Preview launch params");
		pr_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// OnEmulationStarted();
				st_button.setText("Start emulation");
				Thread th = new Thread() {
					public void run() {
						RefreshPrediction();
					}
				};
				th.start();
			}
		});
		this.add(pr_button, gc);

		gc.gridx = 1;
		JButton as_button = new JButton("Accuracy settings");
		as_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// OnEmulationStarted();
				Thread th = new Thread() {
					public void run() {
						GetAccuParams();
					}
				};
				th.start();
			}
		});
		this.add(as_button, gc);

		gc.gridx = 0;
		gc.gridy = 6;
		gc.gridwidth = 2;
		st_button = new JButton("Start emulation");
		st_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (flag) {
					flag = false;
					canvas.getGraphics().clearRect(0, 0, size, size);
					st_button.setText("Start emulation");
					return;
				}				
				Thread th = new Thread() {
					public void run() {
						OnEmulationStarted();
					}
				};
				th.start();
			}
		});
		this.add(st_button, gc);

		gc.gridy = 7;
		gc.gridx = 0;
		gc.gridheight = 5;
		gc.fill=GridBagConstraints.NONE;
		canvas = new Canvas();
		canvas.setSize(size, size);
		this.add(canvas, gc);
		this.pack();
	}

	void OnEmulationStarted() {
		try {
			
			Vector Position = new Vector(new BigDecimal(posx_box.getText()), new BigDecimal(posy_box.getText()));
			Vector Speed = new Vector(new BigDecimal(velx_box.getText()), new BigDecimal(vely_box.getText()));
			BigDecimal Mass = new BigDecimal(mass_box.getText());
			// Parsing inputs
			st_button.setText("Stop emulation");
			flag = true;
			canvas.getGraphics().clearRect(0, 0, size, size);
			canvas.setBackground(Color.white);
			canvas.getGraphics().setColor(Color.black);
			canvas.getGraphics().fillOval(size / 2 - 5, size / 2 - 5, 10, 10);
			Model model = new Model(Position, Speed, Mass);
			model.SetAccuracy((int) accu);
			model.SetStep(new BigDecimal(step));
			model.SetMethod(MethodID);

			int k = (int) Math.pow(10, 2 - (int) Math.log10(step));
			if (k > 1000)
				k = 1000;
			int i = k + 1;
			while (flag) {
				Position = model.PerformStep();
				if (i > k) {
					canvas.getGraphics().fillOval(size / 2 + Position.x, size / 2 - Position.y, 1, 1);
					i = 1;
				}
				i++;
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Wrong number format", "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	void RefreshPrediction() {
		try {
			flag = false;
			Vector Position = new Vector(new BigDecimal(posx_box.getText()), new BigDecimal(posy_box.getText()));
			Vector Speed = new Vector(new BigDecimal(velx_box.getText()), new BigDecimal(vely_box.getText()));
			BigDecimal Mass = new BigDecimal(mass_box.getText());
			canvas.getGraphics().clearRect(0, 0, size, size);
			canvas.setBackground(Color.white);
			canvas.getGraphics().setColor(Color.black);
			canvas.getGraphics().fillOval(size / 2 - 5, size / 2 - 5, 10, 10);
			canvas.getGraphics().fillOval(size / 2 + Position.x - 2, size / 2 - Position.y - 2,5, 5);
			canvas.getGraphics().drawLine(size / 2 + Position.x, size / 2 - Position.y,
					size / 2 + Position.x + (int) (Speed.X.doubleValue() * 10),
					size / 2 - Position.y - (int) (Speed.Y.doubleValue() * 10));
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Wrong number format", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	void GetAccuParams() {
		JTextArea step = new JTextArea();
		step.setText(String.valueOf(this.step));
		JTextArea accu = new JTextArea();
		accu.setText(String.valueOf(this.accu));
		JComboBox<String> meth = new JComboBox<String>(
				new String[] { "Predictor", "Predictor-Corrector", "Runge-Kutta 4" });
		meth.setSelectedIndex(MethodID);
		final JComponent[] inputs = new JComponent[] { new JLabel("Extrapolation step"), step,
				new JLabel("Grid accuracy"), accu, new JLabel("Extrapolation method"), meth };
		boolean flagg = true;
		do {
			int result = JOptionPane.showConfirmDialog(null, inputs, "Accuracy parameters", JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.OK_OPTION) {
				try {
					this.accu = Integer.parseInt(accu.getText());
					this.step = Double.parseDouble(step.getText());
					if (meth.getSelectedIndex() != -1) {
						this.MethodID = meth.getSelectedIndex();
					}
					flagg = false;
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(this, "Wrong number format", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				flagg = false;
			}
		} while (flagg);

	}
}
