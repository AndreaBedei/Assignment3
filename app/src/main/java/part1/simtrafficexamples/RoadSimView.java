package part1.simtrafficexamples;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

import part1.simengineseq.AbstractAgent;
import part1.simengineseq.AbstractEnvironment;
import part1.simengineseq.AbstractSimulation;
import part1.simengineseq.SimulationListener;
import part1.simtrafficbase.*;

import java.awt.*;
import javax.swing.*;

public class RoadSimView extends JFrame implements SimulationListener {

	private RoadSimViewPanel panel;
	private static final int CAR_DRAW_SIZE = 10;

	public RoadSimView(AbstractSimulation simulation) {
		super("RoadSim View");
		setSize(1500, 600);

		panel = new RoadSimViewPanel(1500, 600); // Imposta le dimensioni del pannello
		panel.setSize(1500, 600);

		JPanel cp = new JPanel();

		LayoutManager layout = new BorderLayout();
		cp.setLayout(layout);
		cp.add(BorderLayout.CENTER, panel);

		// Our added code.
		JPanel controlPanel = new JPanel();
		JTextField stepsTextField = new JFormattedTextField("50");
		JButton buttonStart = new JButton("START");
		JButton buttonStop = new JButton("STOP");
		buttonStop.setEnabled(false);

		stepsTextField.setColumns(10);

		// Button start.
		buttonStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int wishedSteps = Integer.parseInt(stepsTextField.getText());

					simulation.setup();	// Useful for reset.
					simulation.run(wishedSteps);

					buttonStart.setEnabled(false);
					buttonStop.setEnabled(true);
				} catch (Exception ex) {
					System.out.println("EXCEPTION! " + ex);
				}
			}
		});

		// Button stop.
		buttonStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulation.stop();
				buttonStart.setEnabled(true);
				buttonStop.setEnabled(false);
			}
		});

		controlPanel.setLayout(new FlowLayout());
		controlPanel.add(stepsTextField);
		controlPanel.add(buttonStart);
		controlPanel.add(buttonStop);
		cp.add(controlPanel, BorderLayout.NORTH);
		setContentPane(cp);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	public void display() {
		SwingUtilities.invokeLater(() -> {
			this.setVisible(true);
		});
	}

	@Override
	public void notifyInit(int t, List<AbstractAgent> agents, AbstractEnvironment env) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyStepDone(int t, List<AbstractAgent> agents, AbstractEnvironment env) {
		var e = ((RoadsEnv) env);
		panel.update(e.getRoads(), e.getAgentInfo(), e.getTrafficLightsInfo());
	}
	
	
	class RoadSimViewPanel extends JPanel {
		
		List<CarAgentInfo> cars;
		List<Road> roads;
		List<TrafficLightInfo> sems;
		
		public RoadSimViewPanel(int w, int h){
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);   
	        Graphics2D g2 = (Graphics2D)g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2.clearRect(0,0,this.getWidth(),this.getHeight());
			
			if (roads != null) {
				for (var r: roads) {
					g2.setColor(new Color(0, 0, 0, 255));
					g2.drawLine((int)r.getFrom().x(), (int)r.getFrom().y(), (int)r.getTo().x(), (int)r.getTo().y());

					for (var s: r.getTrafficLightsInfo()) {
						switch (s.getState()) {
							case RED -> g.setColor(new Color(255, 0, 0, 255));
							case YELLOW -> g.setColor(new Color(255, 255, 0, 255));
							case GREEN -> g.setColor(new Color(0, 255, 0, 255));
						}
						double x, y;
						if (r.getFrom().x() == r.getTo().x()) {
							x = r.getFrom().x();
							y = s.getRoadPos();
						} else {
							x = s.getRoadPos();
							y = r.getFrom().y();
						}

						g2.fillRect((int)x - 5, (int)y - 5, 10, 10);
					}
				}
			}

			
			g.setColor(new Color(0, 0, 0, 255));

			if (cars != null) {
				for (var c: cars) {
					double pos = c.getPos();
					Road r = c.getRoad();
					V2d dir = V2d.makeV2d(r.getFrom(), r.getTo()).getNormalized().mul(pos);
					g2.drawOval((int)(r.getFrom().x() + dir.x() - CAR_DRAW_SIZE/2), (int)(r.getFrom().y() + dir.y() - CAR_DRAW_SIZE/2), CAR_DRAW_SIZE , CAR_DRAW_SIZE);
				}
			}
  	   }
	
	   public void update(List<Road> roads, 
			   			  List<CarAgentInfo> cars,
			   			List<TrafficLightInfo> sems) {
		   this.roads = roads;
		   this.cars = cars;
		   this.sems = sems;
		   repaint();
	   }
	}
}
