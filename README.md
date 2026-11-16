# EBMS

職位管理を題材とした Java Web 開発の学習プロジェクトです。[ruoyi-vue-pro](https://github.com/YunaiV/ruoyi-vue-pro) の職位管理機能のレイヤー構成を参考に、業務ロジック、データベースアクセス、HTTP API、管理画面を段階的に実装します。

現在は、コンソールで実行できる職位登録処理を実装しています。データの保存にはインメモリリポジトリを使用しています。

## 実装済みの機能

- 職位名、職位コード、表示順、状態、備考を登録し、採番された職位 ID を返します。
- 職位名とコードの空白・文字数、表示順の必須入力、状態値を検証します。状態は `0`（有効）または `1`（無効）です。
- 職位名とコードの重複を確認し、不正な入力や重複した登録を拒否します。
- インメモリリポジトリで ID の採番、保存、職位名・コードによる検索を行います。
- 正常登録、不正な入力、職位名・コードの重複、検索結果なしの各ケースをコンソールで検証できます。

## ディレクトリ構成

```text
EBMS/
├── java-basics/
└── post-management/
    ├── pom.xml
    └── src/
        ├── main/java/cn/ebms/post/
        │   ├── Main.java
        │   ├── model/
        │   │   ├── vo/PostSaveReqVO.java
        │   │   └── dataobject/PostDO.java
        │   ├── repository/
        │   │   ├── PostRepository.java
        │   │   └── memory/InMemoryPostRepository.java
        │   └── service/PostServiceImpl.java
        └── test/java/cn/ebms/post/service/
            └── PostServiceImplTest.java
```

`Main` が Service を呼び出し、Service は Repository インターフェースを通じてインメモリストレージにアクセスします。リクエストを `PostSaveReqVO` で受け取り、保存前に `PostDO` に変換します。登録が成功すると、リポジトリで採番された職位 ID を返します。

## 開発環境

- Ubuntu 24.04.4 LTS
- OpenJDK 21（動作確認済みバージョン：21.0.13.0.3）
- Apache Maven 3.8.7（動作確認済み）
- Maven でビルドし、`java` コマンドで実行します。現時点ではアプリケーション用の外部 Java ライブラリやデータベースは不要です。初回ビルド時には Maven プラグインなどのダウンロードが発生します。

## 実行方法

リポジトリのルートディレクトリで、以下を実行します。

```bash
cd post-management
mvn clean compile
java -cp target/classes cn.ebms.post.Main
```

`mvn clean compile` は前回のビルド結果を削除し、ソースコードを `target/classes/` にコンパイルします。`target/` はビルド成果物の出力先であり、Git の管理対象には含めません。

実行すると、各ケースのフィールド値、職位 ID、エラーメッセージ、検索結果がコンソールに表示されます。正常なリクエストは登録され、不正な入力や重複がある場合は登録されません。

## 自動テスト

リポジトリのルートディレクトリで、以下を実行します。

```bash
cd post-management
mvn test
```

JUnit Jupiter 5.13.4 と Maven Surefire 3.5.3 を使用し、以下の21ケースを検証します。パラメータ化テストは入力ごとに1ケースとして数えています。

| 検証対象 | ケース数 |
|---|---:|
| 正常登録 | 1 |
| 職位名・コードの重複拒否と元データの保持 | 2 |
| 職位名・コードの null、空文字、空白、タブ | 8 |
| 職位名50・51文字、コード64・65文字の境界 | 4 |
| リクエスト自体の null、表示順の未設定 | 2 |
| 状態値の null、-1、2、4 | 4 |
| 合計 | 21 |

拒否ケースでは例外だけでなく、対象データが保存されないことも検証します。各ケースは独立したインメモリリポジトリを使用します。

テストコード：`post-management/src/test/java/cn/ebms/post/service/PostServiceImplTest.java`

期待する実行結果：

```text
Tests run: 21, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## 現時点での制限

- HTTP API、認証・認可、フロントエンド画面は未実装です。
- データはメモリ上にのみ保持され、プログラムの終了時に失われます。
- リポジトリはオブジェクト参照を保存・返却するため、検索結果を直接変更すると保存中のデータにも影響します。オブジェクトの分離は未対応です。
- 重複確認と ID の採番はシングルスレッドでの学習用実装です。スレッドセーフではなく、並行実行時の一意性も保証しません。
- `Main` による動作確認と JUnit による自動テストを導入済みです。CI は未導入です。
- 学習段階の実装であり、現時点では本番運用には対応していません。

## 今後の予定

自動テストの拡充、例外処理の統一、Spring による HTTP API、データベースマイグレーションと制約、バックエンドでの権限制御、Vue 管理画面、CI、デプロイ手順を段階的に整備します。

## 参考プロジェクト

[芋道 ruoyi-vue-pro](https://github.com/YunaiV/ruoyi-vue-pro) のリクエストオブジェクト、業務層、データアクセス層、共通レスポンスの設計を参考にしています。本リポジトリは独立した学習プロジェクトであり、同プロジェクトの公式リポジトリではありません。
