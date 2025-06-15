import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import jakarta.annotation.Resource;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import study.online.media.MediaApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@SpringBootTest(classes = MediaApplication.class)
public class TestBigFile {

	@Resource
	private MinioClient minioClient;

	/*测试文件分块*/
	@Test
	public void testChunk() throws IOException {
		File sourseFile = new File("H:\\video\\爱情公寓\\01.爱情公寓1 4K（2009）\\爱情公寓.S01E01.1080p.WEB.mp4");
		String chunkPath = "C:\\Users\\newmaster\\Desktop\\bigfile_test\\chunk\\";

		File chunkFolder = new File(chunkPath);
		if (!chunkFolder.exists()) {
			chunkFolder.mkdirs();
		}

		long chunkSize = 1024 * 1024 * 50;//分块大小

		long chunkNumber = (long) Math.ceil(sourseFile.length() * 1.0 / chunkSize);
		System.out.println("分块总数：" + chunkNumber);

		byte[] bytes = new byte[1024];//缓冲区大小

		RandomAccessFile rafRead = new RandomAccessFile(sourseFile, "r");//使用RandomAccessFile访问文件

		/*分块*/
		for (long i = 0; i < chunkNumber; i++) {
			File file = new File(chunkPath + i);
			if (file.exists()) {
				file.delete();
			}

			boolean flag = file.createNewFile();
			if (flag) {//向分块文件中写入数据
				RandomAccessFile rafWrite = new RandomAccessFile(file, "rw");
				int len;
				while ((len = rafRead.read(bytes)) != -1) {
					rafWrite.write(bytes, 0, len);
					if (file.length() >= chunkSize) {
						break;
					}
				}

				rafWrite.close();
				System.out.println("完成分块" + i);
			}
		}

		rafRead.close();
	}

	@Test
	public void testMerge() throws IOException {
		File chunkFolder = new File("C:\\Users\\newmaster\\Desktop\\bigfile_test\\chunk\\");
		File oringinalFile = new File("H:\\video\\爱情公寓\\01.爱情公寓1 4K（2009）\\爱情公寓.S01E01.1080p.WEB.mp4");

		File file = new File("C:\\Users\\newmaster\\Desktop\\bigfile_test\\爱情公寓.S01E01.1080p.WEB.mp4");
		if (file.exists()) {
			file.delete();
		}

		file.createNewFile();

		RandomAccessFile rafWrite = new RandomAccessFile(file, "rw");
		rafWrite.seek(0); //指针指向文件顶端

		byte[] bytes = new byte[1024];
		File[] files = chunkFolder.listFiles();

		//这里转成集合，易于排序
		Assertions.assertNotNull(files);//这里利用JUnit5的方法断言files不为空
		List<File> fileList = Arrays.asList(files);

		fileList.sort(Comparator.comparingInt(o -> Integer.parseInt(o.getName())));

		/*合并文件*/
		for (File chunkFile : fileList) {
			RandomAccessFile rafRead = new RandomAccessFile(chunkFile, "rw");
			int len;
			while ((len = rafRead.read(bytes)) != -1) {
				rafWrite.write(bytes, 0, len);
			}

			rafRead.close();
		}

		rafWrite.close();

		/*校验文件*/
		try (
			FileInputStream oringinalFileInputStream = new FileInputStream(oringinalFile);
			FileInputStream fileInputStream = new FileInputStream(file)
		) {
			String oringinalMD5 = DigestUtils.md5Hex(oringinalFileInputStream);
			String newlMD5 = DigestUtils.md5Hex(fileInputStream);

			if (oringinalMD5.equals(newlMD5)) {
				System.out.println("文件合并成功，文件HASH:" + newlMD5);
			} else {
				System.out.println("文件合并失败");
			}
		}
	}

	@Test
	public void uploadChunk() {
		String chunkFolderPath = "C:\\Users\\newmaster\\Desktop\\bigfile_test\\chunk\\";

		File chunkFolder = new File(chunkFolderPath);

		/*分块文件*/
		File[] files = chunkFolder.listFiles();

		//这里转成集合，易于排序
		Assertions.assertNotNull(files);//这里利用JUnit5的方法断言files不为空
		List<File> fileList = Arrays.asList(files);

		fileList.sort(Comparator.comparingInt(o -> Integer.parseInt(o.getName())));
		for (File file : fileList) {
			try {
				UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
					.bucket("study-online-mediafiles")
					.object("chunk/" + file.getName())
					.filename(file.getAbsolutePath())
					.build();

				minioClient.uploadObject(uploadObjectArgs);
			} catch (Exception e) {
				System.out.println("上传失败");
			}
		}

		System.out.println("上传成功");
	}
}
