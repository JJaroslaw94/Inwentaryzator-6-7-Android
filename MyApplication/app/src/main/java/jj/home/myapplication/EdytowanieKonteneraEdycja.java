package jj.home.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class EdytowanieKonteneraEdycja extends AppCompatActivity {

    public  boolean czyZrobilemZdjecie = false;
            TextView TekstZNazwa;
            TextView TekstZTagami;
            int IloscElemtentowXML = 0;
            String Nazwa, NazwaPliku, IDkont;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edytowanie_kontenera_edycja);

        TekstZNazwa = (findViewById(R.id.editText));
        TekstZTagami = (findViewById(R.id.editText2));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Nazwa = extras.getString("nazwaAktualnegoInv");
            NazwaPliku = extras.getString("nazwaAktualnegoPliku");
            IDkont = extras.getString("idKontenera");
        }




        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;

        if (CzyIstniejePlik("zasobniki.xml"))
        {
            try {


                // Fragment kodu wczytujacego plik xml przed dodaniem nowych elementow


                db = dbf.newDocumentBuilder();
                FileInputStream fis = openFileInput("zasobniki.xml");
                doc = db.parse(fis);

                NodeList zasobniki = doc.getElementsByTagName("zasobnik");

                for (int i = 0; i < zasobniki.getLength(); i++) {

                    Node city = zasobniki.item(i);

                    if (city.getNodeType() == Element.ELEMENT_NODE) {
                        Element eElement = (Element) city;

                        if (eElement.getAttribute("id").equals(IDkont)) {

                            NodeList cityInfo = city.getChildNodes();


                            for (int j = 0; j < cityInfo.getLength(); j++)
                            {
                                Node info = cityInfo.item(j);
                                if (info.getNodeName().equals("nazwa")) {
                                    TekstZNazwa.setText(info.getTextContent());
                                }else
                                if (info.getNodeName().equals("zdjecie")) {
                                }else
                                if(info.getTextContent().equals("R.drawable.inv")){
                                }else
                                if (info.getNodeName().equals("nazwa_pliku")){

                                }
                                else if (info.getNodeName().equals("wymagane_tagi")){
                                    TekstZTagami.setText(info.getTextContent());
                                }
                            }

                        }


                    }


                }
                fis.close();
            }

            catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        Button PrzyciskTworzacyKontener = (findViewById(R.id.button6));
        PrzyciskTworzacyKontener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(TekstZNazwa.getText().toString())) {
                    TekstZNazwa.setError("Proszę o wypełnienie.");
                } else if (TextUtils.isEmpty(TekstZTagami.getText().toString())) {
                    TekstZTagami.setError("Proszę o podanie przynajmniej jednego tagu.");
                } else {
                    Toast.makeText(EdytowanieKonteneraEdycja.this, "Edytuje ...", Toast.LENGTH_SHORT).show();

                    XmlSerializer serializer = Xml.newSerializer();
                    StringWriter writer = new StringWriter();

                    Document doc = null;
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db;

                    if (CzyIstniejePlik("zasobniki.xml"))
                    {
                        try {
                            serializer.setOutput(writer);
                            serializer.startDocument("UTF-8", true);
                            serializer.startTag("", "zasobniki");

                            // Fragment kodu wczytujacego plik xml przed dodaniem nowych elementow


                            db = dbf.newDocumentBuilder();
                            FileInputStream fis = openFileInput("zasobniki.xml");
                            doc = db.parse(fis);

                            NodeList zasobniki = doc.getElementsByTagName("zasobnik");

                            for (int i = 0; i < zasobniki.getLength(); i++) {

                                Node city = zasobniki.item(i);

                                if (city.getNodeType() == Element.ELEMENT_NODE) {
                                    Element eElement = (Element) city;
                                    System.out.println(eElement.getAttribute("id"));
                                    if (eElement.getAttribute("id").equals(IDkont)) {
                                        serializer.startTag("", city.getNodeName());
                                        serializer.attribute("", "id", Integer.toString(i));

                                        NodeList cityInfo = city.getChildNodes();
                                        for (int j = 0; j < cityInfo.getLength(); j++) {
                                            Node info = cityInfo.item(j);
                                            serializer.startTag("", info.getNodeName());
                                            if(j == 0)
                                            {
                                                serializer.text(TekstZNazwa.getText()+"");
                                            }else
                                            if(j == 1)
                                            {
                                                serializer.text(info.getTextContent());
                                            }else
                                            if(j == 2)
                                            {
                                                serializer.text(TekstZNazwa.getText()+".xml");
                                            }else
                                            {
                                                serializer.text(TekstZTagami.getText()+"");
                                            }

                                            serializer.endTag("", info.getNodeName());
                                        }
                                        serializer.endTag("", city.getNodeName());
                                    }else {
                                        serializer.startTag("", city.getNodeName());
                                        serializer.attribute("", "id", Integer.toString(i));

                                        NodeList cityInfo = city.getChildNodes();
                                        for (int j = 0; j < cityInfo.getLength(); j++) {
                                            Node info = cityInfo.item(j);
                                            serializer.startTag("", info.getNodeName());
                                            serializer.text(info.getTextContent());
                                            serializer.endTag("", info.getNodeName());
                                        }
                                        serializer.endTag("", city.getNodeName());

                                    }
                                }


                            }
                            fis.close();

                            serializer.endDocument();
                            String result = writer.toString();
                            ZapsywanieWczytywanie.ZapisDoPliku(EdytowanieKonteneraEdycja.this, "zasobniki.xml", result);

                            if (CzyIstniejePlik(NazwaPliku))
                            {
                                File file = getBaseContext().getFileStreamPath(NazwaPliku);
                                File file2 = new File((file.getPath().substring(0, file.getPath().length() - NazwaPliku.length()))+TekstZNazwa.getText()+".xml");
                                file.renameTo(file2);
                            }

                            finish();
                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (SAXException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    Intent intentPopUp = new Intent(".StronaStartowa");
                    startActivity(intentPopUp);
                }
            }
        });
    }


}

    public boolean CzyIstniejePlik(String fname) {
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

}
