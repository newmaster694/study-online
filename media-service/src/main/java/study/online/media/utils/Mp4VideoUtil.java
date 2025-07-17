package study.online.media.utils;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Mp4VideoUtil extends VideoUtil {

	String ffmpeg_path;//ffmpeg的安装位置
	String video_path;
	String mp4_name;
	String mp4folder_path;

	public Mp4VideoUtil(String ffmpeg_path, String video_path, String mp4_name, String mp4folder_path) {
		super(ffmpeg_path);
		this.ffmpeg_path = ffmpeg_path;
		this.video_path = video_path;
		this.mp4_name = mp4_name;
		this.mp4folder_path = mp4folder_path;
	}

	//清除已生成的mp4
	private void clear_mp4(String mp4_path) {
		//删除原来已经生成的m3u8及ts文件
		File mp4File = new File(mp4_path);
		if (mp4File.exists() && mp4File.isFile()) {
			mp4File.delete();
		}
	}

	/**
	 * 视频编码，生成mp4文件
	 *
	 * @return 成功返回success，失败返回控制台日志
	 */
	public String generateMp4() {
		//清除已生成的mp4
		clear_mp4(mp4folder_path);

		//ffmpeg.exe -i  lucene.avi -c:v libx264 -s 1280x720 -pix_fmt yuv420p -b:a 63k -b:v 753k -r 18 .\lucene.mp4
		List<String> commend = getCommond();
		String outstring = null;

		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commend);
			//将标准输入流和错误输入流合并，通过标准输入流程读取信息
			builder.redirectErrorStream(true);
			Process p = builder.start();
			outstring = waitFor(p);

		} catch (Exception ex) {
			log.error(ex.getMessage());
		}

		Boolean check_video_time = this.check_video_time(video_path, mp4folder_path);
		if (!check_video_time) {
			return outstring;
		} else {
			return "success";
		}
	}

	@NotNull
	private List<String> getCommond() {
		List<String> commend = new ArrayList<>();

		commend.add(ffmpeg_path);
		commend.add("-i");
		commend.add(video_path);
		commend.add("-c:v");
		commend.add("libx264");
		commend.add("-y");//覆盖输出文件
		commend.add("-s");
		commend.add("1920x1080");
		commend.add("-pix_fmt");
		commend.add("yuv420p");
		commend.add("-c:a");
		commend.add("aac");
		commend.add("-b:a");
		commend.add("128k");
		commend.add("-b:v");
		commend.add("753k");
		commend.add("-r");
		commend.add("18");
		commend.add(mp4folder_path);
		return commend;
	}
}