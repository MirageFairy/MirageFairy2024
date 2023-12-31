# CHANGELOG

## ドキュメンテーションされない変更

各種レシピなどは、REIタブをご参照ください。

## 0.2.0

ハイメヴィスカ

- 追加: 木「ハイメヴィスカ」を追加。
  - 平原系および森林系バイオームにおいて、512チャンクにつき1本自然生成される。
  - 大まかな性質はバニラのオークの原木と共通。
  - 小さい木はなく、すべてが巨木として生成される。
  - 以下のブロックで構成される。
    - ハイメヴィスカの原木
    - 滴るハイメヴィスカの原木
    - ハイメヴィスカの樹洞
    - ハイメヴィスカの葉
- 追加: ブロック「ハイメヴィスカの葉」を追加。
  - 大まかな性質はバニラのオークの葉と共通。
- 追加: ブロック「ハイメヴィスカの原木」を追加。
  - 大まかな性質はバニラのオークの原木と共通。
  - 剣で右クリックすると傷の付いたハイメヴィスカの原木に変わる。
- 追加: ブロック「傷の付いたハイメヴィスカの原木」を追加。
  - 大まかな性質はハイメヴィスカの原木と共通。
  - 時間経過で滴るハイメヴィスカの原木に変わる。
  - 回収時、シルクタッチが無い限りハイメヴィスカの原木に化ける。
- 追加: ブロック「滴るハイメヴィスカの原木」を追加。
  - 大まかな性質はハイメヴィスカの原木と共通。
  - 右クリックでハイメヴィスカの樹液を収穫できる。
    - 収穫後、ブロックは傷の付いたハイメヴィスカの原木に戻る。
    - メインハンドに持っているツールの幸運エンチャントが乗る。
  - 回収時、シルクタッチが無い限りハイメヴィスカの原木に化ける。
  - 回収時、追加でハイメヴィスカの樹液が出る。
    - 幸運1につき入手数が0～1増える。
  - 回収時、1%の確率で追加で妖精のプラスチックが出る。
    - 幸運1につき入手数が0～2増える。
- 追加: ブロック「ハイメヴィスカの樹洞」を追加。
  - 大まかな性質はハイメヴィスカの原木と共通。
  - 回収時、シルクタッチが無い限りハイメヴィスカの原木に化ける。
  - 回収時、追加でフラクタルウィスプが出る。
    - 幸運1につき入手数が0～1増える。
- 追加: ブロック「ハイメヴィスカの板材」を追加。
  - 大まかな性質はバニラのオークの板材と共通。
- 追加: ブロック「ハイメヴィスカの苗木」を追加。
  - 大まかな性質はバニラのオークの苗木と共通。

素材

- 追加: 素材「フラクタルウィスプ」を追加。
  - v2時点において、用途は無し。
- 追加: ブロック「蒼天石ブロック」を追加。
- 追加: 素材「ハイメヴィスカの樹液」を追加。
  - 食べられる。
    - 食べると経験値獲得のステータス効果を1秒間得る。
    - 満腹度の回復量はヴェロペダの実と同等。
  - かまどで精錬1回分の燃料として使える。

その他

- 変更: すべての魔法植物の成長速度を半減。
- 追加: ステータス効果「経験値獲得」を追加。
  - 1秒間に4回、経験値を1だけ得る。

見た目のみ

- 変更: 妖精のプラスチックのテクスチャを変更。
- 追加: ヴェロペダの実のツールチップに説明を追加。

## 0.0.3(v0)

素材

- 追加: 素材「蒼天石」を追加。
  - v0時点において、用途はなし。
- 追加: 素材「蒼天石鉱石」「深層蒼天石鉱石」を追加。
  - バニラのダイヤモンド鉱石に近い性質。
  - 地上世界のY-64～128の領域において、石と深層岩の中に出現する。
  - 蒼天石が得られる。
  - 石のツール以上で採掘可能。

魔法植物システム

- 追加: システム「魔法植物」を追加。
  - 概要
    - 非常に大雑把な性質はバニラの小麦に似ている。
  - 特性群
    - BlockEntityを持ち、追加パラメータとして特性群を保持する。
    - 特性群とは、特性と特性レベルの集まりである。
    - 特性レベルは1010のように二進数で表記される。
    - 地形生成時、特性群はその環境に合わせてランダムに決まる。
    - 魔法植物の各特性の出現条件はREIタブで確認できる。
    - 特性は魔法植物の生育環境によって特定の効果値を上昇させるようにふるまう。
  - 収穫
    - 魔法植物は、特性によって果実枠・葉枠・追加種子・経験値をドロップする。
    - 果実枠と葉枠のドロップアイテムはREIタブで確認可能。
  - 成長
    - 魔法植物は特性によって決まる速さで成長する。
  - 交配
    - 隣接マスに同種の開花した魔法植物がある場合、追加種子が交配された種子になる。
    - 条件を満たす交配対象が複数存在する場合、ランダムな1個が選ばれる。
    - 交配された種子の特性群は、両親の特性群から計算される。
    - まず、両親の各特性の各ビットごとに、どちらの親を受け継ぐかランダムに決める。
    - この際、両親が所持するビットを含む特性は、確定継承特性となる。
    - 次に、特性が15個を超えた分を、確定継承特性以外の中からランダムに消す。
- 追加: 魔法植物の種子アイテムを追加。
  - 追加パラメータとして特性群を保持する。
  - ツールチップに特性群を表示する。
  - メインハンドに同種の種子を持っていた場合、差分がある程度可視化される。
  - 右クリックで魔法植物を植える。
  - クリエイティブの種子は、その場所に自生する特性で魔法植物を植える。
  - 魔法植物の種子はコンポスターに投入可能。
  - 生物学的に種子でないものも、魔法植物においては種子として扱う。

魔法植物

- 追加: 魔法植物「妖花ミラージュ」を追加。
  - 4段階の成長段階を持つ。
  - 上面が平らなブロックおよび、耕地に植えられる。
  - 地上世界の地表、ネザー、エンド外縁の島々において生成される。
  - 地上世界では、大きな輪を作るようにしてスポーンすることがある（妖精の輪）。
  - 果実枠としてミラージュの花粉、葉枠としてミラージュの葉をドロップする。
- 追加: 魔法植物の種子「ミラージュの球根」を追加。
  - ミラージュの種子。
- 追加: 素材「ミラージュの花粉」を追加。
  - v0時点において、用途はなし。
- ミラージュの花粉の圧縮レベルの異なるバリエーションを追加。
  - 「小さなミラージュの花粉」: 1/9倍
  - 「高純度ミラージュの花粉」: 9倍
  - 「特選高純度ミラージュの花粉」: 9×9倍
  - 「厳選高純度ミラージュの花粉」: 9×9×9倍
  - 「激甚高純度ミラージュの花粉」: 9×9×9×9倍
  - 「極超高純度ミラージュの花粉」: 9×9×9×9×倍
- 追加: 素材「ミラージュの葉」を追加。
  - コンポスターに投入可能。
- 追加: 素材「ミラージュの茎」を追加。
  - ミラージュの茎からクラフトできる。
  - 木の棒にクラフトできる。
  - コンポスターに投入可能。
- 追加: 魔法植物「呪草ヴェロペダ」を追加。
  - 大まかな性質はミラージュに準じる。
  - 地上世界の乾燥系バイオームおよびネザーにおいて生成される。
  - 果実枠としてヴェロペダの実、葉枠としてヴェロペダの葉をドロップする。
- 追加: 魔法植物の種子「ヴェロペダの球根」を追加。
  - ヴェロペダの種子。
- 追加: 素材「ヴェロペダの実」を追加。
  - 右クリックで食べられる。
    - 満腹度の回復量は熱帯魚と同じ。
    - 食べると3秒間の持続回復付与。
    - 食べると1%の確率で20秒間の吐き気付与。
    - 食べるのが早い。
  - コンポスターに投入可能。
- 追加: 素材「ヴェロペダの葉」を追加。
  - かまど精錬で鉄塊に変わる。
  - コンポスターに投入可能。

特性

- 以下の特性効果を追加。
  - 「栄養値」: 成長に必須。
  - 「環境値」: 成長に必須。
  - 「成長速度」: 成長速度を増加する。
  - 「種子生成」: 追加種子の生成に必須。
  - 「果実生成」: 果実枠の生成に必須。
  - 「葉面生成」: 葉枠の生成に必須。
  - 「生産能力」: 追加種子・果実枠・葉枠の生産量を増加する。
  - 「経験値」: 経験値の生成に必須。
  - 「幸運係数」: ドロップアイテムに幸運（FortuneおよびLuck）が乗るようになる。
  - 「自然落果」: 自発的に収穫が発生するようになる。
- 以下の特性条件を追加。
  - 地面の土が湿っているとき: 以下のリストに従って効果値に倍率がかかる。
    - 耕地: 湿り気に応じて、0.5～1.0
    - 土系ブロック: 0.5
    - 砂系ブロック: 0.25
  - 地面が結晶質であるとき: 以下のリストに従って効果値に倍率がかかる。
    - ダイヤモンドブロック: 1.0
    - エメラルドブロック: 0.8
    - アメジストブロック: 0.8
    - 金ブロック: 0.6
    - ネザークォーツブロック: 0.6
    - ラピスラズリブロック: 0.4
    - レッドストーンブロック: 0.4
    - 鉄ブロック: 0.4
    - 石炭ブロック: 0.2
    - 銅ブロック: 0.2
    - その他: 0.0
  - 地面が硬いとき: 地面がつるはしで採掘可能なとき、硬度が0～4にかけて0～2の倍率がかかる。
  - 明るいとき: 光量が8～15にかけて、効果値に0～1の倍率がかかる。
  - 暗いとき: 光量が7～0にかけて、効果値に0～1の倍率がかかる。
  - 低温環境のとき: バイオームが対応する条件に該当する場合に有効。
  - 中温環境のとき: バイオームが対応する条件に該当する場合に有効。
  - 高温環境のとき: バイオームが対応する条件に該当する場合に有効。
  - 低湿環境のとき: バイオームが対応する条件に該当する場合に有効。
  - 中湿環境のとき: バイオームが対応する条件に該当する場合に有効。
  - 高湿環境のとき: バイオームが対応する条件に該当する場合に有効。
  - 野外のとき: それより上に何も無い場合に有効。
- 以下の特性を追加。
  - 「エーテル呼吸」: 常に栄養値を増加。
  - 「光合成」: 明るいとき、栄養値を増加。
  - 「闇合成」: 暗いとき、栄養値を増加。
  - 「浸透吸収」: 地面の土が湿っているとき、栄養値を増加。
  - 「鉱物吸収」: 地面が結晶質であるとき、栄養値を増加。
  - 「空気適応」: 常に環境値を増加。
  - 「寒冷適応」: 低温環境のとき、環境値を増加。
  - 「温暖適応」: 中温環境のとき、環境値を増加。
  - 「熱帯適応」: 高温環境のとき、環境値を増加。
  - 「乾燥適応」: 低湿環境のとき、環境値を増加。
  - 「中湿適応」: 中湿環境のとき、環境値を増加。
  - 「湿潤適応」: 高湿環境のとき、環境値を増加。
  - 「種子生成」: 常に種子生成を増加。
  - 「果実生成」: 常に果実生成を増加。
  - 「葉面生成」: 常に葉面生成を増加。
  - 「経験値生成」: 常に経験値を増加。
  - 「妖精の祝福」: 常に幸運係数を増加。
  - 「四つ葉」: 常に幸運係数を増加。
  - 「節状の茎」: 常に成長速度を増加。
  - 「知識の果実」: 常に経験値を増加。
  - 「金のリンゴ」: 常に幸運係数を増加。
  - 「棘状の葉」: 低湿環境のとき、環境値を増加。
  - 「砂漠の宝石」: 低湿環境のとき、に生産能力を増加。
  - 「発熱機構」: 低温環境のとき、環境値を増加。
  - 「浸水耐性」: 高湿環境のとき、環境値を増加。
  - 「高嶺の花」: 常に果実生成を増加。
  - 「肉厚の葉」: 低湿環境のとき、葉面生成を増加。
  - 「自然落果」: 常に自然落果を増加。
  - 「食虫植物」: 野外のとき、栄養値を増加。
  - 「エーテル捕食」: 常に栄養値を増加。
  - 「アスファルトに咲く花」: 地面が硬いとき、栄養値を増加。
  - 「種の繁栄」: 常に種子生成を増加。

その他

- 追加: MirageFairy2024のクリエイティブのタブを追加。
- 追加: システム「ポエム」を追加。
  - 各種追加アイテムのツールチップに表示されるテキスト。
  - 日本語版と英語版では表面的な内容が異なる。

クリエイティブ専用

- 追加: 素材「妖精のプラスチック」を追加。
- 追加: 素材「紅天石」を追加。
- 追加: 素材「夜のかけら」を追加。

## 0.0.2(v0)

- 削除済み。

## 0.0.1(v0)

- 削除済み。
