package de.twenty11.skysail.server.presentation.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.twenty11.skysail.server.presentation.Json2HtmlConverter;

public class Json2HtmlConverterTest {

    private Json2HtmlConverter converter;

	@Before
	public void setUp() throws Exception {
        converter = new Json2HtmlConverter();
	}

	@Test
    @Ignore
	public void convertMinimal() {
		String json = "{}";
        String html = converter.jsonToHtml(json, null);
		String[] lines = html.split("\\n");
		assertThat(lines[0], is(equalTo("<div id=\"json\">")));
		assertThat(lines[1], is(equalTo("{")));
		assertThat(lines[2], is(equalTo("  <ul class=\"obj collapsible\">")));
		assertThat(lines[3], is(equalTo("  </ul>")));
		assertThat(lines[4], is(equalTo("}")));
		assertThat(lines[5], is(equalTo("</div>")));

	}

	@Test
    @Ignore
	public void convertAString() {
		String json = "{\"a\" : \"b\"}";
        String html = converter.jsonToHtml(json, null);
        System.out.println(html);

	}

    @Test
    @Ignore
    public void convertAString2() {
        String json = "{\"a\" : \"b\", \"success\" : true, \"anarray\":[1,2,\"text\"]}";
        String html = converter.jsonToHtml(json, null);
        System.out.println(html);

    }

	@Test
    @Ignore
	public void convertAnArray() {
		String json = "{\"anarray\" : [1,2,\"thr<h1>ee\"]}";
        String html = converter.jsonToHtml(json, null);
		String[] lines = html.split("\\n");
		int i = 3;
		assertThat(lines[i++], is(equalTo("    <li>")));
		assertThat(lines[i++], is(equalTo("      <div class=\"collapser\">-</div>")));
		assertThat(lines[i++], is(equalTo("      <span class=\"prop\">")));
		assertThat(lines[i++], is(equalTo("        <span class=\"q\">\"</span>anarray<span class=\"q\">\"</span>")));
		assertThat(lines[i++], is(equalTo("      </span>: [")));
		assertThat(lines[i++], is(equalTo("      <ul class=\"array collapsible\">")));
		assertThat(lines[i++], is(equalTo("        <li><span class=\"num\">1</span>,</li>")));
		assertThat(lines[i++], is(equalTo("        <li><span class=\"num\">2</span>,</li>")));
		assertThat(lines[i++], is(equalTo("        <li><span class=\"string\">\"thr<h1>ee\"</span></li>")));
		assertThat(lines[i++], is(equalTo("      </ul>")));
		assertThat(lines[i++], is(equalTo("      ]")));
		assertThat(lines[i++], is(equalTo("    </li>")));
	}
	
	@Test
    @Ignore
	public void convertExample() {
		// @formatter:off
		String json = "{\"hey\":\"guy\",\"anumber\":243,\"anobject\":{\"whoa\":\"nuts\",\"anarray\":[1,2,\"thr<h1>ee\"],\"more\":\"stuff\"},\"awesome\":true,\"bogus\":false,\"meaning\":null,\"japanese\":\"\",\"link\":\"http://jsonview.com\", \"notLink\": \"http://jsonview.com is great\",\"aZero\":0,\"emptyString\":\"\"}";
		// @formatter:on
        String html = converter.jsonToHtml(json, null);
		String[] lines = html.split("\\n");
		int i = 3;
	}

	private String readJsonFile(String file) throws Exception {
		File jsonFile = new File("src/test/resources/jsonExamples/" + file);
		FileReader fileReader = new FileReader(jsonFile);
		BufferedReader in = new BufferedReader(fileReader);
		String sCurrentLine;
		StringBuffer json = new StringBuffer();
		while ((sCurrentLine = in.readLine()) != null) {
			json.append(sCurrentLine);
		}
		return json.toString();
	}

	
}
