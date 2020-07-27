package com.repository;

import com.domain.Item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;
//import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    private NamedParameterJdbcTemplate template;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ItemRepository itemRepository;

    private static final RowMapper<Item> ITEM_ROW_MAPPER = (rs, i) -> {

        Item item = new Item();

        item.setId(rs.getInt("id"));
        item.setName(rs.getString("name"));
        item.setDescription(rs.getString("description"));
        item.setPriceM(rs.getInt("price_m"));
        item.setPriceL(rs.getInt("price_l"));
        item.setImagePath(rs.getString("image_path"));
        item.setDeleted(rs.getBoolean("deleted"));

        return item;
    };

   @BeforeEach
    void createTableAndInsert(){

        String createSql = "create table items (\n" +
                "  id integer primary key\n" +
                "  , name text not null\n" +
                "  , description text not null\n" +
                "  , price_m integer not null\n" +
                "  , price_l integer not null\n" +
                "  , image_path text not null\n" +
                "  , deleted boolean default false not null\n" +
                "  , genre integer not null ) ;";

        jdbcTemplate.execute(createSql);

        String insertSql = "INSERT INTO public.items (id, name, description, price_m, price_l, image_path, deleted, genre) VALUES (17, '台湾まぜそば', 'ピリ辛!!の台湾まぜそば汗をジンワリとかく程度の辛さで魚粉の香りが引き立ち、辛く味付けした挽肉が非常にマッチした一品。タレが残ったら追いめしが楽しめます。', 1200, 1300, '17.jpg', false, 7);\n" +
                "INSERT INTO public.items (id, name, description, price_m, price_l, image_path, deleted, genre) VALUES (16, 'まぜ麺Gorgeous4', '食欲をそそるエスニックでスパイシーな一杯。しっかり混ぜて広がるまぜ麺に最後は追い飯をすれば、1度だけでなく2度楽しめるお得感のあるラーメンです。', 780, 880, '16.jpg', false, 7);\n" +
                "INSERT INTO public.items (id, name, description, price_m, price_l, image_path, deleted, genre) VALUES (11, '鶏白湯坦々麺', '鶏の部位でも上質な部位のみを厳選し、旨味を閉じ込めた白湯スープ。10種類のスパイスが組み合わさった爽やかな辛さとナッツの甘味が香ばしさを引き立てた一杯。', 980, 1080, '11.jpg', false, 6);\n" +
                "INSERT INTO public.items (id, name, description, price_m, price_l, image_path, deleted, genre) VALUES (14, '煮干しつけ麺', '煮干しでとられた醤油ベースのスープが喉越しの良いもっちりとした麺の味を引き立てるつけ麺。', 750, 850, '14.jpg', false, 5);\n" +
                "INSERT INTO public.items (id, name, description, price_m, price_l, image_path, deleted, genre) VALUES (3, 'からか麺', '博多絹ごしとんこつをベースに、豆板醤や甜麺醤などを独自に配合した肉味噌を大胆にトッピング。山椒などのスパイスを効かせた自家製ラー油が全体をピリリとまとめあげ、中太のストレート麺がうま味と辛味を余すところなくすくいあげます。1989年に大名本店で登場以来、進化を続ける根強い人気の一杯です。', 800, 900, '3.jpg', false, 4);\n" +
                "INSERT INTO public.items (id, name, description, price_m, price_l, image_path, deleted, genre) VALUES (4, 'かさね味Special', '2001年、本店限定メニューとして誕生。とんこつと鶏ガラを素材に、それぞれの旨みを抽出しながら絶妙なバランスで合わせた鶏豚骨スープは、さっぱりとしながらも深みがあり、加水率の高い中太麺を引き立てています。', 900, 1000, '4.jpg', false, 4);\n" +
                "INSERT INTO public.items (id, name, description, price_m, price_l, image_path, deleted, genre) VALUES (8, '鶏とんこつ麺', '関西のラーメン激戦区で修行を積んだ親父による珠玉の一品。濃厚な鶏の凝縮されたコクのあるスープの旨味と極太麺に海苔の風味が上品かつ風情を醸し出した自信作。', 800, 900, '8.jpg', false, 4);\n" +
                "INSERT INTO public.items (id, name, description, price_m, price_l, image_path, deleted, genre) VALUES (6, '川越とんこつ醤油', '埼玉県・川越のラーメン店と醤油メーカーが中心となって行っている「川越醤油ラーメン」プロジェクトに一風堂も参加！松本醤油商店さんの「はつかり醤油」と川越産の小松菜を一風堂のとんこつスープを合わせた「川越とんこつ醤油」です。', 880, 980, '6.jpg', false, 4);\n" +
                "INSERT INTO public.items (id, name, description, price_m, price_l, image_path, deleted, genre) VALUES (7, '元祖・白丸元味／元祖・赤丸新味', '1985年に創業した博多 一風堂の1号店である大名本店限定メニュー。創業当時のトロリとしたポタージュのようなスープを再現し、通常よりも一段と濃厚な白丸/赤丸に仕上げています。全国のラーメンファン、観光客の方が、この味を求めて足を運んでくださいます。', 900, 1000, '7.jpg', false, 4);\n" +
                "INSERT INTO public.items (id, name, description, price_m, price_l, image_path, deleted, genre) VALUES (2, '赤ラーメン', '自家製の香味油と辛みそを加えることで、一杯のラーメンの中でいくつもの味の奥行きと調和を楽しめます。白丸が正統派のとんこつラーメンならば、赤丸新味は豚骨ラーメンの可能性を広げた“革新派”。 コクと深みを追求した、自信作です。', 750, 850, '2.jpg', false, 4);\n" +
                "INSERT INTO public.items (id, name, description, price_m, price_l, image_path, deleted, genre) VALUES (9, '辛味噌ちゃあしゅう麺', '九州で伝統的に伝わる発酵手法で作られた秘伝味噌をふんだんに使用したコクのある味噌ラーメン。チャーシューは低温調理で豚本来の旨味をぎゅっと閉じ込めた贅沢な一杯。', 820, 920, '9.jpg', false, 3);\n" +
                "INSERT INTO public.items (id, name, description, price_m, price_l, image_path, deleted, genre) VALUES (15, '旨辛味噌麺', '味噌ラーメンの常識を変える一杯。濃厚かつコクと深みのあるスープ、小麦の味がこみ上げる極太麺、ジューシーなチャーシューが胃袋を鷲掴みにする。', 680, 800, '15.jpg', false, 3);\n" +
                "INSERT INTO public.items (id, name, description, price_m, price_l, image_path, deleted, genre) VALUES (1, 'とんこつラーメン', '創業当時から今に引き継ぐとんこつラーメンの本流であり、原点の味。18時間の調理と、丸1日の熟成を経て、とんこつの旨味を極限まで抽出した豊かで香り高いシルキーなスープに、博多らしい細麺がマッチします。', 700, 800, '1.jpg', false, 3);\n" +
                "INSERT INTO public.items (id, name, description, price_m, price_l, image_path, deleted, genre) VALUES (5, '百福元味', 'ラーメンに生涯を捧げた麺翁、日清食品創業者・安藤百福氏を想い、一風堂店主・河原成美が手掛けた醤油ラーメン。 国産の丸鶏を使用したスープ、国産小麦を2種類使用した平打ちちぢれ麺、自家製のごぼう香油など、こだわり抜かれた一杯です。', 980, 1080, '5.jpg', false, 2);\n" +
                "INSERT INTO public.items (id, name, description, price_m, price_l, image_path, deleted, genre) VALUES (10, '追い鰹チャーシュー', '関西のラーメンランキングでNo.1をとったこともある名店で仕込まれた味。香り高い追い鰹でとられたスープはただ飲むだけでなく、添えられた車麩に浸しても楽しめる。ここでしか食べられない極上の一杯。', 1050, 1100, '10.jpg', false, 2);\n" +
                "INSERT INTO public.items (id, name, description, price_m, price_l, image_path, deleted, genre) VALUES (13, '澄み切った醤油らーめん', '喉越しの良さを突き詰めた王道醤油ラーメン。すっきりとした見た目とは裏腹にじっくりのコクのある最後までスープを飲み干したくなる一杯。', 950, 1050, '13.jpg', false, 2);\n" +
                "INSERT INTO public.items (id, name, description, price_m, price_l, image_path, deleted, genre) VALUES (18, '真・澄み切った塩らーめん', '岩塩でキリッとさせた鶏スープがまとまりがある仕上がり。透明度の高いスープは旨味が凝縮された丁寧な一杯。何度でも食べたくなる味のラーメンです。', 990, 1090, '18.jpg', false, 1);\n" +
                "INSERT INTO public.items (id, name, description, price_m, price_l, image_path, deleted, genre) VALUES (12, '貝出汁らーめん原点', '人気の魚「貝」系らーめん。あさりやしじみの貝の旨味を閉じ込め、淡口醤油であっさり仕上げている。', 900, 1000, '12.jpg', false, 1);";


        jdbcTemplate.execute(insertSql);
    }


    @AfterEach
    void dropTable (){
        String dropSql = "DROP TABLE items";
        jdbcTemplate.execute(dropSql);
    }

    @Test
    void findAll() {
        List<Item> list = itemRepository.findAll();

        String sql = "SELECT id, name, description, price_m, price_l, image_path, deleted FROM items";

        List<Item> itemListTest = template.query(sql, ITEM_ROW_MAPPER);

        assertEquals(itemListTest.get(3).getName(), list.get(3).getName());

        assertEquals(itemListTest.size(), list.size());

        assertEquals(itemListTest.get(0).getPriceM(),list.get(0).getPriceM());

    }

    @Test
    void findAllByCheapPrice() {
        List<Item> list = itemRepository.findAllByCheapPrice();

        String sql = "SELECT id, name, description, price_m, price_l, image_path, deleted FROM items ORDER BY price_m ";

        List<Item> itemListTest = template.query(sql, ITEM_ROW_MAPPER);

        assertEquals(itemListTest.get(17).getName(), list.get(17).getName());

        assertEquals(itemListTest.size(), list.size());

        assertEquals(itemListTest.get(4).getPriceL(),list.get(4).getPriceL());
    }

    @Test
    void findAllByExpensivePrice() {
        List<Item> list = itemRepository.findAllByExpensivePrice();

        String sql = "SELECT id, name, description, price_m, price_l, image_path, deleted FROM items ORDER BY price_m DESC";

        List<Item> itemList = template.query(sql, ITEM_ROW_MAPPER);

        assertEquals(itemList.get(5).getName(), list.get(5).getName());

        assertEquals(itemList.size(), list.size());

        assertEquals(itemList.get(4).getPriceL(),list.get(4).getPriceL());


    }

    @Test
    void findAllByPopularItem() {
        List<Item> list = itemRepository.findAllByPopularItem();

        String sql = "SELECT items.id, name, description, price_m, price_l, image_path, deleted FROM items LEFT OUTER JOIN order_items ON items.id=order_items.item_id  GROUP BY items.id ORDER BY count(items.id) DESC, price_m;";

        List<Item> itemList = template.query(sql, ITEM_ROW_MAPPER);

        assertEquals(itemList.toString(), list.toString());
        assertEquals(itemList.get(0).getDeleted() , list.get(0).getDeleted());

    }

    @Test
    void findByNameLike() {

        List<Item> list = itemRepository.findByNameLike("川越");

        String sql = "SELECT id, name, description, price_m, price_l, image_path, deleted FROM items WHERE name LIKE '%川越%' ORDER BY price_m";

        List<Item> itemList = template.query(sql,ITEM_ROW_MAPPER);

        assertEquals(itemList.size(), list.size());

        assertEquals(itemList.get(0).getName(), list.get(0).getName());


    }

    @Test
    void load() {

        Item item = itemRepository.load((long) 4);

        String sql = "SELECT * FROM items WHERE id = 4";

        assertEquals("かさね味Special", item.getName());

        assertEquals("900", item.getPriceM().toString());


    }

    @Test
    void findByGenre() {

        List<Item> list = itemRepository.findByGenre(5);

        String sql = "SELECT * FROM items WHERE genre = 5";

        List<Item> itemList = template.query(sql,ITEM_ROW_MAPPER);

        assertEquals(itemList.size(), list.size());

    }




}