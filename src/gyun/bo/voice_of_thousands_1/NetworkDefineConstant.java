/*
 *   Http 요청 상수값
 */
package gyun.bo.voice_of_thousands_1;

public class NetworkDefineConstant {
	public static final String HOST_URL = "http://52.68.103.73";
	// 웹서버 포트번호
	public static final int PORT_NUMBER = 3000;
	// 저장관련 URL 주소
	public static final String URL = HOST_URL + ":" + PORT_NUMBER;
	public static final String SERVER_URL_VOT_READ_LIST = URL + "/list/ongoing";
	public static final String SERVER_URL_VOT_LISTEN_LIST = URL + "/list/complete";
	public static final String SERVER_URL_VOT_MEMBER_INFO = URL + "/memberinfo"; // 나중에
	public static final String SERVER_URL_VOT_LISTENBOOK_INFO = URL + "/listeninfo";
	public static final String SERVER_URL_VOT_LISTEN_MODE = URL + "/listen";
	public static final String SERVER_URL_VOT_LOGIN = URL + "/login"; // 나중에 수정.
	public static final String SERVER_URL_VOT_UPLOAD_RECORD = URL + "/voiceupload";
	public static final String SERVER_URL_VOT_BOOKING = URL + "/booking";
	public static final String SERVER_URL_VOT_CONNECTIONEND = URL + "/connectionend";
	public static final String SERVER_URL_VOT_SAMPLE = URL + "/sample";
	public static final String SERVER_URL_VOT_HISTORY_DELETE = URL + "/history";
	public static final String SERVER_URL_VOT_PROFILENAME_CHANGE= URL + "/profilename";
	public static final String SERVER_URL_VOT_PROFILEIMAGE_CHANGE= URL + "/profileimage";
	public static final String SERVER_URL_VOT_JOIN= URL + "/join";
	public static final String SERVER_URL_VOT_DUPL_CHECK= URL + "/duplname";
	public static final String SERVER_URL_VOT_MP3_Download= URL + "/Voicedownload";
	
	
	// 요청입력 성공 다이얼로그 id 값
	public static final int VOT_INSERT_DIALOG_OK = 1;
	// 요청입력실패 다이얼로그 id 값
	public static final int VOT_INSERT_DIALOG_FAIL = 2;
}