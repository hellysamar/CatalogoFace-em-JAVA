package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.DAO;
import utils.Validador;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Catalogo extends JFrame {

	// INSTANCIANDO OBJETOS JDBC
	DAO dao = new DAO();
	private Connection conn;
	private PreparedStatement pst;
	private ResultSet rs;
	
	// INSTANCIAR OBJETOS PARA OFLUXO DE BYTES
	private FileInputStream fis;
	
	// VARIÁVEL GLOBAL PARA ARMAZENAR O TAMANHO DA IMAGEM(Bytes)
	private int tamanho;
	

	private static final long serialVersionUID = 1L;
	private JPanel pnl;
	private JLabel lblStatus;
	private JLabel lblData;
	private JLabel lblRegistro;
	private JTextField txtRegistro;
	private JTextField txtNome;
	private JTextField txtEndereco;
	private JLabel lblFoto;
	private JList listaNomes;
	private JScrollPane scrollPaneNomes;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Catalogo frame = new Catalogo();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Catalogo() {
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				statusConn();
				setData();
			}
		});
		setTitle("Catálogo");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Catalogo.class.getResource("/icons/CF.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 681, 392);
		pnl = new JPanel();
		pnl.setBackground(new Color(240, 240, 240));
		pnl.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(pnl);
		pnl.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(128, 128, 255));
		panel.setBounds(0, 293, 742, 60);
		pnl.add(panel);
		panel.setLayout(null);
		
		lblStatus = new JLabel("");
		lblStatus.setIcon(new ImageIcon(Catalogo.class.getResource("/icons/dbOff.png")));
		lblStatus.setBounds(579, 0, 67, 60);
		panel.add(lblStatus);
		
		lblData = new JLabel("");
		lblData.setForeground(SystemColor.text);
		lblData.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblData.setBounds(20, 11, 455, 38);
		panel.add(lblData);
		
		lblRegistro = new JLabel("Número de Registro:");
		lblRegistro.setBackground(new Color(240, 240, 240));
		lblRegistro.setForeground(new Color(128, 128, 255));
		lblRegistro.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblRegistro.setBounds(10, 26, 144, 17);
		pnl.add(lblRegistro);
		
		txtRegistro = new JTextField();
		txtRegistro.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		txtRegistro.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				String caracteres = "0123456789";
				
				if (!caracteres.contains(e.getKeyChar() + "")) {
					e.consume();
				}
			}
		});
		txtRegistro.setBounds(165, 26, 134, 20);
		pnl.add(txtRegistro);
		txtRegistro.setColumns(10);
		txtRegistro.setDocument(new Validador(6));
		
		JLabel lblNome = new JLabel("Nome:");
		lblNome.setBackground(new Color(240, 240, 240));
		lblNome.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNome.setForeground(new Color(128, 128, 255));
		lblNome.setBounds(10, 68, 46, 14);
		pnl.add(lblNome);
		
		txtNome = new JTextField();
		txtNome.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				listarNomesBanco();
			}
		});
		txtNome.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		txtNome.setBounds(10, 93, 289, 20);
		pnl.add(txtNome);
		txtNome.setColumns(10);
		txtNome.setDocument(new Validador(30));
		
		JButton btnCarregarFoto = new JButton("Carregar foto");
		btnCarregarFoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carregarFoto();
			}
		});
		btnCarregarFoto.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		btnCarregarFoto.setForeground(new Color(128, 128, 255));
		btnCarregarFoto.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnCarregarFoto.setBounds(479, 259, 115, 23);
		pnl.add(btnCarregarFoto);
		
		JButton btnAdicionar = new JButton("");
		btnAdicionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adicionar();
			}
		});
		btnAdicionar.setToolTipText("Adicionar");
		btnAdicionar.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		btnAdicionar.setIcon(new ImageIcon(Catalogo.class.getResource("/icons/CF check.png")));
		btnAdicionar.setBounds(10, 192, 90, 90);
		pnl.add(btnAdicionar);
		
		JButton btnEditar = new JButton("");
		btnEditar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				editar();
			}
		});
		btnEditar.setToolTipText("Editar");
		btnEditar.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		btnEditar.setIcon(new ImageIcon(Catalogo.class.getResource("/icons/CF edit.png")));
		btnEditar.setBounds(110, 192, 90, 90);
		pnl.add(btnEditar);
		
		JButton btnDeletar = new JButton("");
		btnDeletar.setToolTipText("Deletar");
		btnDeletar.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		btnDeletar.setIcon(new ImageIcon(Catalogo.class.getResource("/icons/CF delete.png")));
		btnDeletar.setBounds(210, 192, 90, 90);
		pnl.add(btnDeletar);
		
		JButton btnLimpar = new JButton("");
		btnLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetCampos();
			}
		});
		btnLimpar.setToolTipText("Limpar campos");
		btnLimpar.setIcon(new ImageIcon(Catalogo.class.getResource("/icons/CF change.png")));
		btnLimpar.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		btnLimpar.setBounds(310, 192, 90, 90);
		pnl.add(btnLimpar);
		
		scrollPaneNomes = new JScrollPane();
		scrollPaneNomes.setVisible(false);
		scrollPaneNomes.setBorder(null);
		scrollPaneNomes.setBounds(10, 111, 289, 82);
		pnl.add(scrollPaneNomes);
		
		listaNomes = new JList();
		listaNomes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				listarNomeBanco();
			}
		});
		listaNomes.setBorder(null);
		scrollPaneNomes.setViewportView(listaNomes);
		
		JLabel lblEndereco = new JLabel("Endereço:");
		lblEndereco.setBackground(new Color(240, 240, 240));
		lblEndereco.setForeground(new Color(128, 128, 255));
		lblEndereco.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblEndereco.setBounds(10, 124, 78, 14);
		pnl.add(lblEndereco);
		
		txtEndereco = new JTextField();
		txtEndereco.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		txtEndereco.setBounds(10, 149, 390, 20);
		pnl.add(txtEndereco);
		txtEndereco.setColumns(10);
		txtEndereco.setDocument(new Validador(30));
		
		lblFoto = new JLabel("New label");
		lblFoto.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblFoto.setIcon(new ImageIcon(Catalogo.class.getResource("/icons/CF photo.png")));
		lblFoto.setBounds(410, 11, 229, 242);
		pnl.add(lblFoto);
		
		JButton btnPesquisar = new JButton("");
		btnPesquisar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buscarRegistro();
			}
		});
		
		btnPesquisar.setToolTipText("Pesquisar");
		btnPesquisar.setIcon(new ImageIcon(Catalogo.class.getResource("/icons/CF search.png")));
		btnPesquisar.setBounds(310, 23, 90, 90);
		pnl.add(btnPesquisar);
	}
	
	// Método para verificar o Status de conexão
	private void statusConn() {
		try {
			conn = dao.conectar();
			
			if (conn == null) {
//				System.out.println("Sem conexão");
				lblStatus.setIcon(new ImageIcon(Catalogo.class.getResource("/icons/dbOff.png")));
			} else {
//				System.out.println("Conectado!");
				lblStatus.setIcon(new ImageIcon(Catalogo.class.getResource("/icons/dbOn.png")));
			}
			
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void setData() {
		Date data = new Date();
		
		DateFormat formatar = DateFormat.getDateInstance(DateFormat.FULL);
		
		lblData.setText(formatar.format(data));
	}
	
	private void carregarFoto() {
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Selecione a foto do usuário para o catálogo");
		jfc.setFileFilter(new FileNameExtensionFilter("Arquivo de imagem(*.png, *.jpg, *.jpeg)", "png", "jpg", "jpeg"));
		int resultado = jfc.showOpenDialog(this);
		
		if (resultado == JFileChooser.APPROVE_OPTION) {
			try {
				 fis = new FileInputStream(jfc.getSelectedFile());
				 tamanho = (int) jfc.getSelectedFile().length();
				 Image foto = ImageIO.read(jfc.getSelectedFile()).getScaledInstance(lblFoto.getWidth(), lblFoto.getHeight(), Image.SCALE_SMOOTH);
				 lblFoto.setIcon(new ImageIcon(foto));
				 lblFoto.updateUI();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
	
	private void adicionar() {
		if (txtNome.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Preencha o campo Nome!");
			txtNome.requestFocus();
			
		} else if (txtEndereco.getText().isEmpty()) {
			JOptionPane.showInternalMessageDialog(null, "Preencha o campo Endereço!");
			txtEndereco.requestFocus();
//		} else if () {
			
		} else {
			
			String inserir = "INSERT INTO tblUsuarios (nome, foto, endereco) VALUES (?, ?, ?)"; // CREATE do CRUD
			
			try {
				conn = dao.conectar();
				pst = conn.prepareStatement(inserir);
				pst.setString(1, txtNome.getText());
				pst.setBlob(2, fis, tamanho);
				pst.setString(3, txtEndereco.getText());
				
				int confirma = pst.executeUpdate();
				
				if (confirma == 1) {
					JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!");
					resetCampos();
					
				} else {
					JOptionPane.showMessageDialog(null, "Erro! Usuário não cadastrado!");
				}
				
				conn.close();
				
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
	
	private void editar() {
		String sqlUpdate = "UPDATE tblUsuarios SET nome = ?, foto = ?, endereco = ? WHERE registro = ?";
		
		try {
			conn = dao.conectar();
			pst = conn.prepareStatement(sqlUpdate);
			pst.setString(1, txtNome.getText());
			pst.setBlob(2, fis, tamanho);
			pst.setString(3, txtEndereco.getText());
			pst.setString(4, txtRegistro.getText());
			
			int editou = pst.executeUpdate();
			
			if (editou == 1) {
				JOptionPane.showInternalMessageDialog(null, "Edição realizada com sucesso!");
			} else {
				JOptionPane.showInternalMessageDialog(null, "Edição não realizada!");
			}
		} catch (Exception e) {
			System.out.println("ocorreu uma excessão ao tentar editar o usuário");
		}
	}
	private void listarNomesBanco() {
		// criando um vetor dinamico
		DefaultListModel<String> modelo = new DefaultListModel<>();
		listaNomes.setModel(modelo);
		String LerLista = "SELECT * FROM tblUsuarios WHERE nome LIKE '" + txtNome.getText() + "%' ORDER BY nome;";
		
		try {
			conn = dao.conectar();
			pst = conn.prepareStatement(LerLista);
			rs = pst.executeQuery();
			
			while(rs.next()) {
				scrollPaneNomes.setVisible(true);
				modelo.addElement(rs.getString(2));
				
				if(txtNome.getText().isEmpty()) {
					scrollPaneNomes.setVisible(false);
				}
			}
			conn.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}
//		scrollPaneNomes.setVisible(true);
	}
	
	private void listarNomeBanco() {
		int linha = listaNomes.getSelectedIndex();
		if (linha >= 0) {
			String sql = "SELECT * FROM tblUsuarios WHERE nome LIKE '" + txtNome.getText() + "%' ORDER BY nome LIMIT " + (linha) + ", 1";
			
			try {
				conn = dao.conectar();
				pst = conn.prepareStatement(sql);
				rs = pst.executeQuery();
				
				while (rs.next()) {
					scrollPaneNomes.setVisible(false);
					txtRegistro.setText(rs.getString(1));
					txtNome.setText(rs.getString(2));
					txtEndereco.setText(rs.getString(4));
					
					Blob blob = (Blob) rs.getBlob(3);
					byte[] img = blob.getBytes(1, (int) blob.length());
					
					BufferedImage imagem = null;
					try {
						imagem = ImageIO.read(new ByteArrayInputStream(img));
					} catch (Exception e) {
						System.out.println(e);
					}
					
					ImageIcon icone = new ImageIcon(imagem);
					Icon foto = new ImageIcon(icone.getImage().getScaledInstance(lblFoto.getWidth(), lblFoto.getHeight(), Image.SCALE_SMOOTH));
					
					lblFoto.setIcon(foto);
				}
			} catch (Exception e) {
				 System.out.println(e);
			}
		} else {
			scrollPaneNomes.setVisible(false);
		}
	}
	
	private void buscarRegistro() {		
		if (txtRegistro.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Preencha o campo Registro para buscar");
			txtRegistro.requestFocus();
		} else {
			String buscarR = "SELECT * FROM tblUsuarios WHERE registro = ?";
			
			try {
				conn = dao.conectar();
				pst = conn.prepareStatement(buscarR);
				pst.setString(1, txtRegistro.getText());
				rs = pst.executeQuery();
				if (rs.next()) {
					txtNome.setText(rs.getString(2));
					txtEndereco.setText(rs.getString(4));
					
					Blob blob = (Blob) rs.getBlob(3);
					byte[] img = blob.getBytes(1, (int) blob.length());
					BufferedImage imagem = null;
					try {
						imagem = ImageIO.read(new ByteArrayInputStream(img));
					} catch (Exception e) {
						System.out.println(e);
					}
					ImageIcon icone = new ImageIcon(imagem);
					Icon foto = new ImageIcon(icone.getImage().getScaledInstance(lblFoto.getWidth(), lblFoto.getHeight(), Image.SCALE_SMOOTH));
					
					lblFoto.setIcon(foto);
				} else {
					JOptionPane.showMessageDialog(null, "Usuário não encontrado!");
					resetCampos();
				}
				conn.close();
			} catch (Exception e) {
				System.out.println(e);
			}

		}
		
	}
	
	private void resetCampos() {
		txtRegistro.setText(null);
		txtNome.setText(null);
		txtEndereco.setText(null);
		lblFoto.setIcon(new ImageIcon(Catalogo.class.getResource("/icons/CF photo.png")));
		txtNome.requestFocus();
		scrollPaneNomes.setVisible(false);
	}
}
