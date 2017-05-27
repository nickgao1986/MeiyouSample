//package nickgao.com.meiyousample.love;
//
//import android.app.Activity;
//
//import com.meetyou.calendar.activity.period.PeriodAnalysisOneActivity;
//import com.meetyou.calendar.activity.temp.TemperatureAnalysisOneActivity;
//import com.meetyou.calendar.activity.weight.WeightAnalysisOneActivity;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 分析与建议常量数据
// *
// * @author ziv
// */
//public class AnalysisConstants {
//
//    /**
//     * 经期分析与建议
//     */
//    public static final List<String[][]> mPeriodAdivices = new ArrayList<String[][]>();
//
//    static {
//        mPeriodAdivices.add(new String[][]{
//                {
//                        "压力大，精神高度紧张，内分泌功能失调或长期服用避孕药。",
//                        "青春期女性往往是因为下丘脑一垂体一卵巢功能未完全发育成熟。",
//                        "黄体功能不全、卵巢储备功能不良、子宫本身病变、更年期、严重的子宫肌瘤也有可能导致。"
//                },
//                {
//                        "保持良好的心情，不要有过大的压力。",
//                        "合理的饮食安排，均衡营养。",
//                        "适当服用调经丸、乌鸡白凤丸进行调节。",
//                        "较为严重者建议到医院就诊。"
//                },
//                {
//                        "周期过短"
//                }
//        });
//        mPeriodAdivices.add(new String[][]{
//                {
//                        "过度节食，营养不均衡使体内大量脂肪和蛋白质被耗用，造成雌激素出现合成障碍。",
//                        "长期的精神压抑、生闷气或遭受重大精神刺激和心理创伤造成情绪异常。",
//                        "放置节育器，患盆腔炎症、子宫内膜炎等。"
//                },
//                {
//                        "保持精神愉快，避免精神刺激和情绪波动。",
//                        "避免生、冷、辛、辣等刺激性食物，多饮开水，保持大便通畅。",
//                        "注意卫生，预防感染，注意外生殖器的卫生清洁。",
//                        "穿着柔软、透气的内裤，并勤洗勤换。"
//                },
//                {
//                        "周期过长"
//                }
//        });
//        mPeriodAdivices.add(new String[][]{
//                {
//                        "气的生成不足，或消耗过度而致气的功能减退造成气血虚弱。",
//                        "外界环境和自身生理因素导致湿气重，经络瘀阻不通的情况。"
//                },
//                {
//                        "日常避免久坐，常活动腰腹，注意保暖。",
//                        "吃的营养易消化，如青菜，豆腐，绿豆粥，鲜奶，各类蛋，鱼等；",
//                        "多喝红糖生姜汤。"
//                },
//                {
//                        "经期过短"
//                }
//        });
//        mPeriodAdivices.add(new String[][]{
//                {
//                        "平时心情郁闷，或在行经期，生产后感受到寒邪，形成瘀血阻滞胞脉，新血不能归经。",
//                        "属阴虚体质，或者因久病耗伤阴液、生产过多，房事过度。",
//                        "子宫肌瘤、子宫内膜异位症等妇科问题也会是诱因。"
//                },
//                {
//                        "适当服用黄体酮或者中药益母草颗粒调节月经。",
//                        "平时生活上要尽量有规律，防止受寒，多吃含有铁和滋补性的食物。",
//                        "调整好自己的心态，保持心情舒畅。",
//                        "较为严重者建议到医院就诊。"
//                },
//                {
//                        "经期过长"
//                }
//        });
//        mPeriodAdivices.add(new String[][]{
//                {
//                        "多由情志不舒，或外邪侵袭引起肝气久郁不解所致气滞血瘀。",
//                        "体质较弱，气血不足；偏胖，缺乏运动。"
//                },
//                {
//                        "注意保暖，热水泡脚，热敷小腹等可助血行及排寒。",
//                        "保持心情愉悦，肝气舒畅了，血液就不会瘀滞。",
//                        "适当运动，促进气血流通。",
//                        "多吃高蛋白低油脂的肉类（猪，鸡鸭，牛肉等），五谷杂粮、坚果等，补气血。"
//                },
//                {
//                        "流量过少"
//                }
//        });
//        mPeriodAdivices.add(new String[][]{
//                {
//                        "爱吃辛辣煎炸的食物、心情郁闷，思虑过度导致阳盛体质，即气热。",
//                        "身体虚弱，久病后脾气受损、太劳累、吃过多寒凉的食物造成中气虚弱。",
//                        "小产、人工流产的病史，其体内比较容易积聚瘀血。"
//                },
//                {
//                        "饮食以素淡为主，如少吃肉。",
//                        "多吃蔬菜水果，多吃坚果五谷，补气血不上火。",
//                        "不熬夜，睡眠足；生活有规律。 "
//                },
//                {
//                        "流量过多"
//                }
//        });
//        mPeriodAdivices.add(new String[][]{
//                {
//                        "工作压力大：长期超负荷的工作，会导致女性对疼痛过分敏感。",
//                        "先天素体偏寒，肾阳不足，阳气虚、阴寒内盛，或后天受凉引起宫寒。",
//                        "体重过度肥胖、经常吸烟或盆腔大量充血。"
//                },
//                {
//                        "注意生活规律，多休息，避免劳累过度，尤其是经期要防寒避湿。",
//                        "防止过度节食，不宜过食生冷食物，如苦瓜、番茄、藕、竹笋、空心菜、鲤鱼、文蛤、海带、梨、西瓜、香蕉等。",
//                        "保持心情舒畅，加强锻炼，提高身体素质。"
//                },
//                {
//                        "轻微痛经"
//                }
//        });
//        mPeriodAdivices.add(new String[][]{
//                {
//                        "情志不遂或病邪侵扰，阻遏肝脉出现肝郁气滞。",
//                        "全身性血脉不畅，造成血脉瘀滞或阻塞。",
//                        "子宫内膜异位症或引发重度痛经。长期憋尿也是诱因经甚至会引发不孕。"
//                },
//                {
//                        "补气养血，即多食葡萄、荔枝、花生、胡萝卜、菠菜、红枣等。",
//                        "注意少吃辛辣寒凉之物（如辣椒、海鲜等），避免使用冷水或冷饮食，以免加重疼痛症状。",
//                        "保持心情舒畅和精神的放松。",
//                        "较为严重者建议到医院就诊。"
//                },
//                {
//                        "严重痛经"
//                }
//        });
//        mPeriodAdivices.add(new String[][]{
//                {
//                        "一般月经周期是28～30天，但是也有40天来一次的。只要有规律性，均属于正常情况。",
//                        "月经容易受多种因素影响，提前或错后3～5天，也是正常现象。"
//                },
//                {
//                        "月经期要保持外阴清洁，适当锻炼，注意经期卫生。",
//                        "注意调节情绪，劳逸结合。",
//                        "进行合理饮食。",
//                        "避免房事，不能乱用药物。"
//                },
//                {
//                        "经期正常"
//                }
//        });
//
//        mPeriodAdivices.add(new String[][]{
//                {
//
//                },
//                {
//                        "正确使用经期卫生用品，当卫生巾使外阴发生发痒、不适或异味时，应马上停止使用，另选其他品牌。",
//                        "月经期勤换卫生巾，最好两小时更换一次。每天用温热水清洗外阴，最好采用淋浴。",
//                        "出现经期紧张症状时吃低盐饮食，适当减少乳品和甜食，增加纤维素摄取量，多吃瘦肉、全麦、荞麦、大麦及深绿叶蔬菜等有助于缓解情绪、消除水肿和乳房胀痛及减轻疲劳的食物。",
//                        "有饮茶或饮咖啡习惯的女性，容易发生经期紧张症，因此经期不宜引用含大量咖啡因的饮品，避免诱发或加重经期紧张症。"
//                },
//                {
//                        "多次记录分析更准确哦！"
//                }
//        });
//    }
//
//
//    /**
//     * 爱爱分析与建议
//     */
//    public static final List<String[][]> mLoveAdivices = new ArrayList<String[][]>();
//
//    static {
//        mLoveAdivices.add(new String[][]{
//                {
//                        "大姨妈来了就表示没有怀孕。从最近一次姨妈来了都没有爱爱记录。"
//                },
//                {
//                        "中医认为生育过早，女性的精血会大量受损，会导致体内正气衰退。晚育也会对胎儿健康和发育不利。男性的婚育年龄在24-28岁，女性生育的年龄在21-28岁，男女最佳生育年龄在女28-30，男28-32岁。"
//                },
//                {
//                        "怀孕机率为零"
//                }
//        });
//        mLoveAdivices.add(new String[][]{
//                {
//                        "使用避孕套，理论上的成功率可达95%左右，实际上由各种因素造成的失败率可达15%左右，所以使用避孕套还是有可能怀孕的。"
//                },
//                {
//                        "频繁吃避孕药会严重影响月经的周期，导致内分泌失调，严重的还出现多囊卵巢或不孕的后果。",
//                        "建议服用避孕药的女性多运动，如果出现不适要及时就诊。"
//                },
//                {
//                        "怀孕机率极低"
//                }
//        });
//        mLoveAdivices.add(new String[][]{
//                {
//                        "如果周期短，可能在月经结束几天后就会排卵。考虑到精子在输卵管中的存活时间能达到3天，因此理论上讲，它有可能一直待到女性排卵，使她在月经后期很短的时间内受孕。"
//                },
//                {
//                        "经期爱爱极容易引起月经病和生殖器官的炎症，甚至可能造成不孕。应该在月经干净2-3天后再爱爱为宜"
//                },
//                {
//                        "怀孕机率较低"
//                }
//        });
//        mLoveAdivices.add(new String[][]{
//                {
//                        "女性排卵的时间，受外界环境、气候、本人的情绪，以及健康状态等因素影响，从而出现排卵推迟或提前，并且还有可能发生额外排卵，所以说安全期也有可能不安全哦。"
//                },
//                {
//                        "卵巢在一个月经周期中先后排两次卵的机会是极少的，即排卵后到下次月经来潮前这段时间一般不会再发生第二次排卵，所以，排卵后安全期比较安全。"
//                },
//                {
//                        "怀孕机率低"
//                }
//        });
//        mLoveAdivices.add(new String[][]{
//                {
//                        "我们把排卵日的前5天和后4天称为排卵期，排卵期爱爱容易怀孕。但受孕是一个复杂的过程，受很多因素的影响。正常情况下每个月都会排卵，但排卵期爱爱不一定怀孕。"
//                },
//                {
//                        "排卵前1周，每2天爱爱1次，这样可以在保证精子质量的前提下，提前、或准时到达输卵管、与卵子会合形成受精卵。爱爱的最佳时间，是在下午5-7时。因为精子数量、或质量这段时间可达到高峰，此时女性也最易受孕。"
//                },
//                {
//                        "怀孕机率较高"
//                }
//        });
//        mLoveAdivices.add(new String[][]{
//                {
//                        "我们把排卵日的前5天和后4天称为排卵期，排卵期爱爱容易怀孕。但受孕是一个复杂的过程，受很多因素的影响。正常情况下每个月都会排卵，但排卵期爱爱不一定怀孕。"
//                },
//                {
//                        "排卵前1周，每2天爱爱1次，这样可以在保证精子质量的前提下，提前、或准时到达输卵管、与卵子会合形成受精卵。爱爱的最佳时间，是在下午5-7时。因为精子数量、或质量这段时间可达到高峰，此时女性也最易受孕。"
//                },
//                {
//                        "怀孕机率高"
//                }
//        });
//        mLoveAdivices.add(new String[][]{
//                {
//                        "我们把排卵日的前5天和后4天称为排卵期，排卵期爱爱容易怀孕。但受孕是一个复杂的过程，受很多因素的影响。正常情况下每个月都会排卵，但排卵期爱爱不一定怀孕。"
//                },
//                {
//                        "排卵前1周，每2天爱爱1次，这样可以在保证精子质量的前提下，提前、或准时到达输卵管、与卵子会合形成受精卵。爱爱的最佳时间，是在下午5-7时。因为精子数量、或质量这段时间可达到高峰，此时女性也最易受孕。"
//                },
//                {
//                        "怀孕机率很高"
//                }
//        });
//        mLoveAdivices.add(new String[][]{
//                {
//                        "女性的排卵日期一般在下次月经来潮前的14天左右。卵子自卵巢排出后在输卵管的内能生存1-2天，以等待受精；男子的精子在女子的生殖道内可维持2-3天受精能力，故在卵子排出的前后几天里爱爱容易受孕。"
//                },
//                {
//                        "通过各种方法预测排卵日是提高受孕率比较可靠的方法，对于想要宝宝的父母来说，应该多了解一些有关预测排卵日的手段，选择排卵日爱爱是提高受孕率的关键，所以要学会测基础体温、观察自己的宫颈黏液的性状、测尿LH，如果没有器质性病变，坚持数月一定会有“喜”的。"
//                },
//                {
//                        "怀孕机率非常高"
//                }
//        });
//        mLoveAdivices.add(new String[][]{
//                {
//                        "怀孕前3个月是容易发生流产的时期，尤其是有流产高风险的孕妇，所以应避免或减少爱爱。",
//                        "怀孕中期，胎盘已形成，妊娠较稳定，可以适度地爱爱。孕中期适度地爱爱，有益于夫妻恩爱和胎儿的健康发育。",
//                        "孕晚期提倡尽可能停止爱爱，尤其是孕36周后严禁爱爱，以免发生意外。"
//                },
//                {
//                        "特殊体质孕妇前置胎盘的孕妇本来就容易出血，需要休息，不宜爱爱；高血压、糖尿病等孕妇，应等病情得到控制后，才可爱爱。",
//                        "怀孕期间的爱爱不宜过于剧烈。",
//                        "爱爱后出现异常症状要去医院检查。"
//                },
//                {
//                        "怀孕期"
//                }
//        });
//    }
//
//    /**
//     * 美体分析与建议
//     */
//    public static final List<String[][]> mWeightAdivices = new ArrayList<String[][]>();
//
//    static {
//        mWeightAdivices.add(new String[][]{
//                {
//                        "生活上遇到难题或工作压力大让女性转向吃东西寻求安慰。",
//                        "婚后舒适的生活，在饮食方面毫无节制。",
//                        "怀孕、产后身材走样了，运动量太小或几乎都没有运动。"
//                },
//                {
//                        "找到瘦身的动力，坚定信心。",
//                        "控制饮食，降低热量的摄取，少吃脂肪类食物，如蛋糕、油条、炸鸡等。",
//                        "增加身体活动机会，减少静态生活方式，且运动时要获得愉快、趣味和成就感。"
//                },
//                {
//                        "身材偏胖"
//                }
//        });
//        mWeightAdivices.add(new String[][]{
//                {
//                        "偏食、饮食习惯不良所造成的营养摄取的不足与不均衡。",
//                        "压力或疾病（如胃溃疡、易腹泻或便秘）等因素﹐造成肠胃的消化及吸收出了问题。",
//                        "暴饮暴食增加肠胃负担，无法有效吸收，引起消化不良。"
//                },
//                {
//                        "增加富含蛋白质的食物，如肉、蛋、鱼、豆制品等；",
//                        "正餐之间可补充营养品、点心；",
//                        "经常运动，提高内脏器官的功能，增强食欲，促进营养吸收；",
//                        "勿给自己过大的压力，生活要规律，不焦虑。"
//                },
//                {
//                        "身材偏瘦"
//                }
//        });
//        mWeightAdivices.add(new String[][]{
//                {
//                        "饮食习惯良好，生活有规律。",
//                        "女性每个月的月经周期中，体重会有1～2公斤的落差都是正常的现象。"
//                },
//                {
//                        "保持良好的饮食习惯并且注意均衡营养。",
//                        "适当的锻炼；保持轻松愉快的心情。"
//                },
//                {
//                        "身材保持良好"
//                }
//        });
//        mWeightAdivices.add(new String[][]{
//                {
//                        "由于药物的副作用，或遗传或营养过度；有肥胖家族史。",
//                        "“油”吃太多，糖吃太多，过量蛋白质摄入。",
//                        "肾上腺皮质功能亢进，皮脂分泌过多；胰岛素分泌过多，代谢率降低；甲状腺功能减退。"
//                },
//                {
//                        "多做运动，养成运动的习惯。",
//                        "远离零食、甜品和酒精。",
//                        "低脂肪、低热量饮食；慢慢进食，细嚼慢咽。",
//                        "多喝水，增加膳食纤维的食物量。"
//                },
//                {
//                        "身材肥胖"
//                }
//        });
//
//
//        mWeightAdivices.add(new String[][]{
//                {
//                        "原本属于偏瘦型的人，体重可能无法顺利地增长。孕妇体重过轻易生出低体重儿。体重低于2500克的新生儿称为低体重儿。这样的新生儿皮下脂肪少，保温能力差，呼吸机能和代谢机能都比较弱，易感染疾病，增加了养育的困难。"
//                },
//                {
//                        "对于身体瘦弱、体重少于正常值的孕妇，怀孕期间应尽量多吃，在医生的指导下，增加食物摄入量，这样才能使身体有足够的体能和热量，负担得起孕育健康宝宝的使命。"
//                },
//                {
//                        "孕前BMI<18.5"
//                }
//        });
//        mWeightAdivices.add(new String[][]{
//                {
//                        "对于体型标准的孕妈咪来说，只要平常注意不要让体重急剧增长，并做一点适度的运动，体重一般不都不会有太大问题。"
//                },
//                {
//                        "孕前身体健康、营养均衡的准妈，只需在医生的指导下适当补充孕期所需的食物和营养，保证优质蛋白、维生素、矿物质、微量元素的摄入，不必刻意地大补特补。",
//                        "通过运动控制孕期体重，如散步， Kegel练习，游泳等"
//                },
//                {
//                        "孕前18.5≤BMI≤23.9"
//                }
//        });
//        mWeightAdivices.add(new String[][]{
//                {
//                        "原来是偏胖型的孕妈咪，为了不产生妊娠高血压疾病，必须进行更加严格的体重管理。"
//                },
//                {
//                        "在医生的指导下适当补充孕期所需的食物和营养，保证优质蛋白、维生素、矿物质、微量元素的摄入",
//                        "通过运动控制孕期体重，如散步， Kegel练习，游泳等",
//                        "请准爸爸每天确认准妈妈的体重数字，为老婆的体重严格把关，共同完成好孕期体重控制的工作。"
//                },
//                {
//                        "孕前BMI>23.9"
//                }
//        });
//    }
//
//
//    /**
//     * 怀孕指数分析与建议
//     */
//    public static final List<String[][]> mTemperatureAdivices = new ArrayList<String[][]>();
//
//    static {
//        mTemperatureAdivices.add(new String[][]{
//                {
//                        "基础体温通常会随着排卵而升高，月经周期的前半期，体温波动在36.6度以下，排卵后转入月经周期的后半期，体温较前半期升高，波动在36.6度至37度之间，体温上升表示已排卵，约持续两个礼拜的高温期后，体温再度降低，然后又轮到每月一次的经期。"
//                },
//                {
//                        "避免一些剧烈运动，避免疲劳，情绪不稳定等不适。",
//                        "咖啡，茶等饮料会增加焦虑不安的情绪，要少喝，可改喝大麦茶。",
//                        "避免吃太冰的食物，如冰淇淋、冰西瓜等。"
//                },
//                {
//                        "正常体温"
//                }
//        });
//        mTemperatureAdivices.add(new String[][]{
//                {
//                        "基础体温通常会随着排卵而升高，月经周期的前半期，体温波动在36.6度以下，排卵后转入月经周期的后半期，体温较前半期升高，波动在36.6度至37度之间，体温上升表示已排卵，约持续两个礼拜的高温期后，体温再度降低，然后又轮到每月一次的经期。",
//                        "体温没有上升表示没有排卵。"
//                },
//                {
//                        "最好到医院就诊，检查是什么原因造成没有排卵，以便对症下药，及早治愈！"
//                },
//                {
//                        "未排卵体温"
//                }
//        });
//        mTemperatureAdivices.add(new String[][]{
//                {
//                        "如果怀孕了，基础体温会持续在高温状态（36.6度至37度之前），但是持续一段时间（超过20天）后突然下降(36.6度以下)，表示黄体功能不足或胎盘功能不良，有流产倾向。"
//                },
//                {
//                        "最好到医院检查确认一下。",
//                        "一旦出现出血，4～5天的安心静养是很必要的。",
//                        "生活有规律，注意个人卫生。",
//                        "防寒保暖，增强免疫。"
//                },
//                {
//                        "疑似早期流产体温"
//                }
//        });
//        mTemperatureAdivices.add(new String[][]{
//                {
//                        "基础体温通常会随着排卵而升高，月经周期的前半期，体温波动在36.6度以下，排卵后转入月经周期的后半期，体温较前半期升高，波动在36.6度至37度之间，体温上升表示已排卵，约持续两个礼拜的高温期后，体温再度降低，然后又轮到每月一次的经期。如果怀孕了，基础体温就不会下降，而持续在高温状态，体温持续高温是因为怀孕后卵巢分泌的荷尔蒙量增加之故。"
//                },
//                {
//                        "可能有宝宝了，最好去医院检查一下哦",
//                        "怀孕前若有用药需提供药单请医师评估",
//                        "保持轻松的心情，多休息少做激烈活动，怀孕初期禁房事",
//                        "调整饮食，蛋白质、叶酸要补足"
//                },
//                {
//                        "怀孕体温"
//                }
//        });
//
//        mTemperatureAdivices.add(new String[][]{
//                {
//                        "怀孕后基础体温会升高，也就是处于我们正常体温（36.2~36.5）的高温期（比正常体温高0.4~0.5左右），到怀孕三个月时体温还是处于高温期，到十四周左右基础体温开始下降，直到分娩都处于正常体温状态。"
//                },
//                {
//                        "人体在较长时间（6小时）的睡眠后醒来，尚未进行任何活动之前所测量到的体温称之为基础体温。",
//                        "多休息、多喝水、饮食清淡可以缓解孕早期体温较高时的身体不舒服。",
//                        "孕早期内体温突然下降至36.7℃以下建议最好到医院检查一下。"
//                },
//                {
//                        "怀孕体温"
//                }
//        });
//    }
//
//
//    /**
//     * 症状分析与建议
//     */
//    public static final List<String[][]> mSymptomsAdivices = new ArrayList<String[][]>();
//
//    static {
//        mSymptomsAdivices.add(new String[][]{
//                {
//                        "工作中用眼过度、长时间专注荧屏、压力太大等。",
//                        "不爱运动、夜生活过多或睡眠不足、饮食不当等不良习惯诱发偏头痛。",
//                        "经期偏头痛：经血大量流失造成气血亏损，致血气运行不畅，血淤则不通，不通则痛。"
//                },
//                {
//                        "放松心情和身体，间或闭上眼睛或到室外做些简易舒展运动，打开窗户让室内空气流通。",
//                        "保证睡眠充足，不要过于紧张和劳累，保持心情愉快等。",
//                        "注意生活规律，月经来潮前夕要注意休息。",
//                        "如果头痛难耐，最好去看医生，不要一味服用止痛药"
//                }
//        });
//        mSymptomsAdivices.add(new String[][]{
//                {
//                        "月经期间女性的抵抗力会下降，体质较为虚弱的情况引起。",
//                        "生育过多、人流过频、性生活过度。",
//                        "运动后第二天出现全身酸痛的症状。"
//                },
//                {
//                        "平时注意保养，增强体质，在月经期间不要劳累。",
//                        "多喝白开水、多吃蔬菜水果；保持心情舒畅。",
//                        "合理安排运动；运动后用温热水泡洗可减轻肌肉酸痛。"
//                }
//        });
//        mSymptomsAdivices.add(new String[][]{
//                {
//                        "当经期来临，有限的气血被调集，肠胃越发地气血不足，会出现身体功能削弱、气血不足。",
//                        "性生活或分娩时损伤宫颈，使细菌侵入造成子宫糜烂导致经期腹泻。",
//                        "食物不洁引发肠胃疾病引起腹泻"
//                },
//                {
//                        "经期一定要注意饮食，尽量以清淡、易消化的食物（小米粥、蔬菜等）为主，切忌暴饮暴食。",
//                        "多喝热水，多休息，注意给小肚子保暖。"
//                }
//        });
//        mSymptomsAdivices.add(new String[][]{
//                {
//                        "主要是身体虚弱或头部供血不足引起。",
//                        "过度疲劳或睡眠不足为眩晕症的诱发因素之一。",
//                        "抑郁恼怒等精神刺激可致肝阳上亢或肝风内动，而诱发眩晕。"
//                },
//                {
//                        "可以吃大枣，枸杞，鱼汤，鸡汤，调整饮食结构。",
//                        "加强锻炼，减少对外界的应激反应，改变昼夜生活节奏。",
//                        "应胸怀宽广，精神乐观，心情舒畅，情绪稳定"
//                }
//        });
//        mSymptomsAdivices.add(new String[][]{
//                {
//                        "经前体内雌激素水平增高、乳腺增生、乳房间组织水肿引起，通常在月经来潮后胀痛会消失。",
//                        "情绪不畅或生活压力过大，肝郁气滞，引发乳房胀痛",
//                        "黄体素分泌减少，雌激素相对增多造成乳腺增生。"
//                },
//                {
//                        "多吃低脂肪、高纤维的饮食的，远离含有咖啡因的食物（如咖啡、汽水、巧克力、茶等）。",
//                        "经期胸罩可以适当大一点。",
//                        "采用热敷或者冷热交替着敷来缓解乳房的胀痛。",
//                        "经常按摩乳房，可以使过量的体液再回到淋巴系统，而且还能预防乳腺疾病的发生。"
//                }
//        });
//
//        mSymptomsAdivices.add(new String[][]{
//                {
//                        "内分泌功能失调，雄性激素分泌过多。",
//                        "性激素水平、皮脂腺大量分泌、痤疮丙酸杆菌增殖，毛囊皮脂腺导管的角化异常及炎症等因素相关。"
//                },
//                {
//                        "用温水洗脸，不用油脂类化妆品，以减少皮肤的油腻。",
//                        "在饮食方面建议不要吃辛辣刺激的、油腻的、甜的食物，多吃蔬菜水果及粗粮。",
//                        "保持大便通畅，不要熬夜。"
//                }
//        });
//        mSymptomsAdivices.add(new String[][]{
//                {
//                        "上班族由于疲劳或精神紧张，可能导致暂时性食欲不振。",
//                        "过食、过饮、过度节食、运动量不足、慢性便秘。",
//                        "月经来临前及期间，身体内分泌发生变化。女性在怀孕初期，或由于口服避孕药的副作用，也可能导致食欲不振或呕吐。"
//                },
//                {
//                        "利用具有香味、辣味、苦味的食物，来刺激并且提高胃液的分泌及增进食欲",
//                        "平时吃饭要慢，多吃点蛋白质高的食物。可以多吃点山药，南瓜。",
//                        "在进食过程中，保持精神愉快。"
//                }
//        });
//        mSymptomsAdivices.add(new String[][]{
//                {
//                        "压力、刺激、兴奋、焦虑时；生病时；至高海拔的地方；或者睡眠规律改变时(如时差；轮班的工作等)都会导致失眠。",
//                        "月经前和经期由于体内的雌激素分泌增多，对睡眠有一定干扰。",
//                        "轻度神经系统不稳定。"
//                },
//                {
//                        "晚上尽量少吃难消化或油腻或有刺激味的食物；避免服用兴奋饮料（如咖啡、浓茶等），不吸烟。",
//                        "注意减轻大脑兴奋状态，睡前做体操适当放松，不要带着问题上床。",
//                        "睡前喝杯牛奶，洗澡，写日记，听一会音乐，或洗个热水脚，坚持睡前的习惯性活动。"
//                }
//        });
//        mSymptomsAdivices.add(new String[][]{
//                {
//                        "可能是月经期生理性低热的表现，从中医上来分析，多为阴虚发热的范畴。",
//                        "如果没有特别的不适，是不会有问题的。"
//                },
//                {
//                        "经期内调节好饮食，饮食忌辛辣生冷（如辣椒、苦瓜等），多饮水。",
//                        "如果效果不好的话，或者体温超高的话，考虑有合并感染的情况，建议去医院做一下检查。",
//                        "适当服用调经丸、乌鸡白凤丸进行调节。"
//                }
//        });
//        mSymptomsAdivices.add(new String[][]{
//                {
//                        "子宫内膜血管会发生强烈的收缩，从而导致月经量锐减，甚至发生闭经、痛经。",
//                        "刺激胃肠道，使胃肠道的蠕动发生紊乱，出现胃痛、食欲不振、大便失调。",
//                        "吃大量冷饮，还会引发咽喉部位炎症。"
//                },
//                {
//                        "饮料温度最好介于30-32摄氏度。",
//                        "吃饭前后不宜吃冷饮。",
//                        "当吃一些鱼类、瘦肉、鸡肉、蛋、奶制品等，同时多吃豆制品、新鲜蔬菜和瓜果。",
//                        "经期注意保暖，在冷气房里要多穿衣服；可以把姜拍碎放一些红糖开水冲服对经期有很好的保健。"
//                }
//        });
//
//        mSymptomsAdivices.add(new String[][]{
//                {
//                        "由于月经前期激素水平的升高导致的，疼痛比较轻微；一般属正常情况。",
//                        "腹腔脏器的炎症，如胃肠炎、胰腺炎、胆囊炎、阑尾炎、腹膜炎等引起。"
//                },
//                {
//                        "肚子痛要卧床休息一下，用热水袋放肚子上暖暖，会舒服点。",
//                        "注意平时经期的卫生，建议少食生冷刺激性食物（辣椒、大蒜、黄瓜等），可以喝红糖水。",
//                        "经期间心情舒畅，避免不良刺激，以防月经不调。"
//                }
//        });
//        mSymptomsAdivices.add(new String[][]{
//                {
//                        "月经前期宫内膜脱落或经期抵抗力下降，腰部受凉招风导致腰痛。",
//                        "身体过度疲劳，不正常的站、坐姿势，常穿高跟鞋等。",
//                        "办公室久坐，长时间保持一个姿势。"
//                },
//                {
//                        "做好保暖工作，如晚上烫个热水脚，贴暖宝宝在腰部等。",
//                        "上班时间时不时变换一下坐姿，隔一段时间就站起来伸个懒腰",
//                        "适当参与一些舒缓的运动，如太极拳或瑜伽都可以"
//                }
//        });
//        mSymptomsAdivices.add(new String[][]{
//                {
//                        "体质虚弱所引起的。",
//                        "由子宫下垂或月经崩漏未完全治愈所致。"
//                },
//                {
//                        "经期的时候多喝红糖水可以活血化瘀。",
//                        "要注意保暖，注意卫生。"
//                }
//        });
//        mSymptomsAdivices.add(new String[][]{
//                {
//                        "月经前期激素紊乱的情况伤害到体内的组织，如果在肠胃附近，就会造成腹泻、肠胃疼痛甚至呕吐等。",
//                        "精神紧张，健康状况减退。"
//                },
//                {
//                        "不要吃生冷食，如苦瓜、猕猴桃、黄瓜、西瓜等。",
//                        "不要过度操劳，多卧床休息。"
//                }
//        });
//        mSymptomsAdivices.add(new String[][]{
//                {
//                        "月经前体内激素发生变化，会导致有轻微便秘的症状，月经后消失或明显缓解。",
//                        "水喝太少、吃太多油炸食品、饮食太单一、蔬菜水果摄入太少等，此外，消化系统不好、经常熬夜、缺乏运动等等，都会引发便秘。"
//                },
//                {
//                        "养成定时大便习惯。",
//                        "不要久站久坐，一个小时就要活动一下。",
//                        "禁辛辣食物；多吃蔬菜，补充食物纤维；每天清晨喝一杯清水或盐水，有助胃肠代谢。",
//                        "跳绳可锻炼腹肌，帮助排便。"
//                }
//        });
//        mSymptomsAdivices.add(new String[][]{
//                {
//                        "看颜色：白带异常主要表现在颜色变化上，当出现黄色或黄绿色脓性白带时，伴有外阴、阴道瘙痒情况，多半是妇科炎症引起。而血白带，常见于宫颈息肉、宫内节育器引起的副作用、重度慢性宫颈炎等疾病。",
//                        "看性状：当白带状如豆渣呈絮状，伴有阴道奇痒，多因霉菌感染或患糖尿病引起。而清澈如水，常湿透内裤，有臭味，这种情况多是输卵管肿瘤征兆。当白带呈泡沫状，量多，伴有瘙痒，这种多是滴虫所致。"
//                },
//                {
//                        "定期检查，每年至少做一次全面的妇科体检。",
//                        "无论出现何种情况的白带增多或其他不适，都应立即去医院诊治。",
//                        "不能随便使用洗液，一定要在医生指导下用药，否则可能加重病情。",
//                        "少穿紧身裤，尤其不要穿紧身尼龙内裤，最好选择棉质内裤。",
//                        "担心白带弄脏内裤，喜欢用卫生护垫，易造成外阴滋生大量细菌。",
//                        "锻炼身体，保证睡眠，多食高维食品，学会调节情绪，免疫力自然增强。"
//                }
//        });
//        //感冒
//        mSymptomsAdivices.add(new String[][]{
//                {
//                        "普通感冒又称上呼吸道感染，是包含鼻腔、咽或喉部急性炎症的总称。",
//                        "受凉、淋雨、气候突变、过度疲劳等原因会导致全身或呼吸道局部防御功能降低，可使原已存在于上呼吸道的或从外界侵入的病毒或细菌迅速繁殖，从而诱发本病。",
//                        "老幼体弱，免疫功能低下或患有慢性呼吸道疾病的患者易感。"
//                },
//                {
//                        "避免受凉、淋雨、过度疲劳；避免与感冒患者接触，避免脏手接触口、眼、鼻。",
//                        "孕妈要坚持适度有规律的户外运动，提高机体免疫力与耐寒能力是预防本病的主要方法。",
//                        "免疫调节药物和疫苗：对于经常、反复发生本病以及老年免疫力低下的患者，可酌情应用免疫增强剂。",
//                        "目前除流感病毒外，尚没有针对其他病毒的疫苗。"
//                }
//        });
//        //孕吐
//        mSymptomsAdivices.add(new String[][]{
//                {
//                        "怀孕初期，体内绒毛膜促使性腺激素分泌量明显增加，而使胃酸显著减少，随之消化酶的活性也降低。",
//                        "因而，不但影响孕妇的肠胃道正常消化功能，而且还会使孕妇产生头晕、恶心、呕吐、食欲不振、肢体乏力等妊娠反应，又称“孕吐”，这是一种正常的生理现象。",
//                },
//                {
//                        "孕妇的孕吐程度和心理上的作用有明显的关系，因此孕妈在孕吐较严重的时候，可以将自己的注意力进行转移，多关注美好的事物。",
//                        "同时，饮食上选择比较适合自己口味的食物，并且在每次食用的时候，坚持做到量少进餐，要多吃些新鲜蔬菜和和水果。",
//                        "另外，运动可以缓解孕吐症状；每天腾出一点时间，到户外舒展下自己的肢体，让心情放松，孕吐便轻松缓解了。"
//                }
//        });
//        //嗜睡
//        mSymptomsAdivices.add(new String[][]{
//                {
//                        "怀孕期间，孕妈妈因绒毛膜性腺激素增加，使得身体容易疲累，生活作息不正常而有嗜睡的现象。",
//                        "此外，怀孕后嗜睡还是一种荷尔蒙分泌的结果。", "怀孕后身体会分泌出来一种黄体荷尔蒙使子宫内壁变得柔软，避免孕妈流产。",
//                        "但是，另一方面这种荷尔蒙又有麻醉作用，导致人体的行动变得迟缓，因而感到困倦。",
//                        "孕妇新陈代谢加快，妊娠期母体分泌系统产生变化，体内热量消耗快，血糖不足，都会让孕妈妈昏昏欲睡。"
//                },
//                {
//                        "当作息时间回归正常，嗜睡的情况就自然消失了。",
//                        "可以做些自己感兴趣的事情来转移注意力；不过孕妈需要充分的休息。",
//                        "如果无法抑制嗜睡症状，也不必太过刻意的控制自己。",
//                        "这种嗜睡也不会维持太久，进入妊娠第14~15周左右，胎盘完整形成后，就不太会感到什么睡意了。",
//                        "另外建议准妈咪可以少量多餐，维持血糖一定浓度;此外，疲倦时不妨小睡片刻，但最好不要超过一小时，以免夜里失眠。"
//                }
//        });
//        //浮肿
//        mSymptomsAdivices.add(new String[][]{
//                {
//                        "孕期浮肿发生的原因有很多，妊娠子宫压迫下腔静脉，使静脉血液回流受阻；胎盘分泌的激素及肾上腺分泌的醛固酮增多，造成体内钠和水分滞留；体内水分积存，尿量相应减少;母体合并较重的贫血，血浆蛋白低，水分从血管内渗出到周围的组织间隙等等。", "不过别担心，在生完宝宝后体内滞留的水分也会渐渐排出，孕妈的水肿现象也会随之消失的。"
//                },
//                {
//                        "腿部浮肿较严重时，孕妈应多卧床休息，侧躺能减轻对静脉的压力。",
//                        "由于下腔静脉在身体右侧，最好能向左侧躺卧，这样可以避免压迫到下肢静脉，并减少血液回流的阻力。",
//                        "另一方面，为了消除浮肿，孕妈必须保证血液循环畅通、气息顺畅，所以在注意保暖的同时尽量避免穿着过紧的衣服。"
//                }
//        });
//        //
//        mSymptomsAdivices.add(new String[][]{
//                {
//                        "怀孕的前3个月，孕妈特别容易感到频尿，到了孕期的第4个月，由于子宫出了骨盆腔进入腹腔中，因此症状就会慢慢地减缓，进入孕晚期，大约38周，由于胎头下降，使得子宫再次重回骨盆腔内，尿频症状又变得越发明显，甚至发生漏尿。",
//                        "此时如果孕妇仅是小便多，但没有发热、腰痛、尿混浊等症状，均为正常现象，不需要特殊处理，宝宝出生后症状自然会消失。",
//                        "若于解尿时有疼痛感，或尿急得无法忍受时，很有可能是因为膀胱发炎或感染细菌。"},
//                {
//                        "缓解尿频的症状，孕妈可以适当控制水分和盐分的摄入。",
//                        "为了避免在夜间频繁上厕所，你可以从傍晚时就减少喝水。",
//                        "将喝水时间前移，睡觉前1～2小时内不要再喝水。",
//                        "白天喝水要少量多次，不要一次大量饮水。",
//                        "有利尿作用的茶、咖啡等，孕妈妈要少喝;西瓜、冬瓜、黄瓜、西红柿等不要吃得过多。",
//                        "排尿时身体前倾能帮助孕妈妈排空膀胱，减少残留在膀胱中的尿液。"
//                }
//        });
//        //便秘
//        mSymptomsAdivices.add(new String[][]{
//                {
//                        "孕妈怀孕后，在内分泌激素变化的影响下，胎盘分泌大量的孕激素，使胃酸分泌减少、胃肠道的肌肉张力下降及肌肉的蠕动能力减弱；",
//                        "从而导致吃进去的食物在胃肠道停留的时间加长，不能像孕前那样正常排出体外。",
//                        "此外，孕期补钙期间是最易引起便秘。",
//                        " 因为钙本身属于吸收率较低的矿物元素，加之孕期胃肠功能减弱，肠蠕动慢，肠内容物在肠内停留时间长，使得水分吸收造成大便干结。"
//                },
//                {
//                        "建议准妈妈多食含纤维素多的蔬菜、水果和粗杂粮，如芹菜、绿叶菜、萝卜、瓜类、苹果、香蕉、梨、燕麦、杂豆、糙米等;",
//                        "另外，起床后可先空腹饮一杯温开水或蜂蜜水，再吃早饭，这可促进起床后的直立反射和胃结肠反射。",
//                        "便很快就会产生便意，长期坚持就会形成早晨排便的好习惯;",
//                        "此外，适量的运动可以增强准妈妈的腹肌收缩力，促进肠道蠕动，预防或减轻便秘。",
//                        "因此，准妈妈即使在身体日益沉重时，也应该做一些力所能及的运动，如散步、适当做点轻松的家务等，以增加肠道的排便动力。"
//                }
//        });
//        mSymptomsAdivices.add(new String[][]{
//                {
//                        "造成这种孕期疲劳感的原因可能是荷尔蒙的改变，特别是孕酮(也叫黄体酮)的急剧增加；",
//                        "此外，当你感觉不舒服或频繁起夜上厕所的时候，睡眠质量就更会受到影响；",
//                        "恶心和呕吐也会消耗你的精力，让你疲劳；各种所有的因素加在一起，就造成了孕期疲劳，你会觉得自己的一天就好像马拉松比赛一样难熬。"
//                },
//                {
//                        "要缓解孕期的这种疲惫，你得开始顺应身体的自然需要，每天提早上床睡觉，并养成每天午睡的习惯。",
//                        "尽量调整时间安排，取消不必要的社交活动，家务活也可以暂时放到一边。",
//                        "同时，保证健康合理的饮食，会让你觉得更有精力。",
//                        "每天多休息几次，伸伸懒腰、做做深呼吸，这样你就能摆脱疲劳感，更轻松地度过这段时期。"
//                }
//        });
//        mSymptomsAdivices.add(new String[][]{
//                {
//                        "怀孕后，孕妈妈体内激素有所变化，容易导致孕妈出现短期过敏性皮肤炎。",
//                        "另外受到雌性激素和孕激素的影响，孕妈妈皮肤内层会产生玄色素的玄色体活细胞数量几率大大增加，玄色素集中的地方会形成明显的色素沉着，肚子中心的白线会变成茶色，乳头和乳晕、腋下四周出现玄色的情况也不少见。",
//                        "而且增加的雌性激素会导致肝脏所分泌的胆汁受阻不能顺畅流出，从而引起皮肤的严重瘙痒性问题。"
//                },
//                {
//                        "预防肌肤缺水的首要工作是清洁，孕期皮肤十分敏感，每次洗脸时应使用温和无刺激的洁面用品，配合冷水或温水洗脸，最后使用毛巾轻压脸颊，将水吸干；忌用力擦拭，否则会弄伤表层皮肤；由于皮肤敏感，洗脸的次数应相对减少，每日两次即可。",
//                        "洗完后可用温和的润肤霜均匀擦于面部。"
//                }
//        });
//        mSymptomsAdivices.add(new String[][]{
//                {
//                        "在怀孕16周以后，胎动会变得逐渐明显起来，怀孕35周左右是宝宝胎动最活跃的时期。",
//                        "正常情况下，每个宝宝胎动都会呈现一定规律性，而当胎动出现异常时，孕妈应该慎重对待。",
//                        "一般而言，胎动的次数以及快慢强弱等表示着胎宝宝的健康情况。",
//                        "但由于胎宝宝个体的差异、孕周的不同等，胎动次数并非恒定不变，只要胎动有规律、有节奏就表示胎宝宝是健康的。"
//                },
//                {
//                        "孕妇自怀孕的第28周起，准妈咪可以在每天的晚餐后(此时胎动较频繁)，采左侧卧姿势，并对每天数胎动做好记录。",
//                        "当察觉胎动异常时，孕妈应注意休息，不要过度操劳，注意随气温变化增减衣物，避免感冒;",
//                        "尽量避免到人多的地方去;经常开窗通风，保持室内的空气流通，适当进行锻炼;多喝水、多吃新鲜的蔬菜和水果。"
//                        , "孕晚期，孕妈要每天坚持数胎动，一旦有异常，马上去医院检查。"
//                }
//        });
//        mSymptomsAdivices.add(new String[][]{
//                {
//                        "孕妈体内黄体酮的增加，导致孕期分泌物出现变化。",
//                        "由于黄体酮增加，孕妈妈的新陈代谢也会更加活跃，加上体内雌激素的作用，阴道上皮细胞及宫颈腺体分泌旺盛，导致子宫颈及阴道壁里的水分和血管里的血液比平时增多，因此分泌物也随之增多，而到了孕晚期，孕妈的身体为了给宝宝的出生做好润滑准备，阴道的分泌物也会随之增加。",
//                        "这是属于正常的生理现象，孕妈不必担心。"
//                },
//                {
//                        "当分泌物增加时，孕妈可以选择具有吸收效果的护垫，保证阴部干爽；同时也要注意勤于更换，避免感染。",
//                        "另外，可以每天用温水进行清洁，避免使用肥皂及洗液，以免消灭了阴部的益生菌，反而降低了孕妈的抵抗力。",
//                        "当孕妈发现自己的分泌物颜色变为浓黄色，并且伴有瘙痒，疼痛等感觉的时候，可能是阴部已经感染阴道炎。",
//                        "这时，孕妈必须要医院进行分泌物检查，根据医生的建议选择治疗方案。"
//                }
//        });
//
//
//    }
//
//    /**
//     * 通过类名获取分析所有结果
//     *
//     * @param className
//     * @return
//     */
//    public static List<String[][]> getAdivices(Activity className) {
//        try {
//            String name = className.getClass().getName();
//            if (name.equals(PeriodAnalysisOneActivity.class.getName())) {
//                return mPeriodAdivices;
//            } else if (name.equals(WeightAnalysisOneActivity.class.getName())) {
//                return mWeightAdivices;
//            } else if (name.equals(LoveAnalysisOneActivity.class.getName())) {
//                return mLoveAdivices;
//            } else if (name.equals(TemperatureAnalysisOneActivity.class.getName())) {
//                return mTemperatureAdivices;
//            } else {
//                return mSymptomsAdivices;
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return new ArrayList<String[][]>();
//
//    }
//
//    private static final String[] SYMPTOMS_DEFINE = new String[]{
//            "头部疼痛，包括头的前、后、偏侧部疼痛和整个头部疼痛。",
//            "懒、头晕、腿沉。持续性的，很长时间都不能缓解的疲劳感。身体疼痛伴有酸软感觉。",
//            "一种常见症状，指排便次数明显超过平日习惯的频率，粪质稀薄，水分增加，每日排便量超过200g，或含未消化食物或脓血、粘液。",
//            "一般认为眩晕是人的空间定位障碍所致的一种主观错觉，对自身周围的环境、自身位置的判断发生错觉。一般来说，头晕、头昏相对较轻，而眩晕则较重。",
//            "疼痛且有胀的感觉。胸等处胀痛，时发时止，多属于气滞之症。",
//            "是与毛囊一致的圆锥形丘疹，不发红也不隆起与皮面，数量少则不易察觉，用手可以触及含在皮肤中的米粒大的皮损。包括白头粉刺和黑头粉刺。",
//            "指进食的欲望降低。完全的不思进食则称厌食。",
//            "指无法入睡或无法保持睡眠状态，导致睡眠不足。因为各种原因引起入睡困难、睡眠深度或频度过短、早醒及睡眠时间不足或质量差等，是一种常见病。",
//            "又称发烧。判定是否发热，最好是和自己平时同样条件下的体温相比较。如不知自己原来的体温，则腋窝体温（检测10分钟）超过37.4℃可定为发热。",
//            "冷饮过量，轻则胃胀难受，重则引起消化不良或胃肠炎、腹泻等。",
//            "指由于各种原因引起的腹腔内外脏器的病变，而表现为腹部的疼痛。腹痛可分为急性与慢性两类。病因极为复杂，包括炎症、肿瘤、出血、梗阻、穿孔、创伤及功能障碍等。",
//            "自觉腰部酸楚不适的表现。",
//            "小腹即小肚子，是指肚脐以下的部份，坠即下坠，胀即胀满。小腹坠胀多见于妇科疾病妊娠或子宫疾病，或者分泌排泄系统。",
//            "胃内容物反入食管，经口吐出的一种反射动作。可将咽入胃内的有害物质吐出，是机体的一种防御反射，但频繁而剧烈地呕吐会引起脱水、电解质紊乱等并发症。",
//            "是临床常见的复杂症状，而不是一种疾病，主要是指排便次数减少、粪便量减少、粪便干结、排便费力等。上述症状同时存在2种以上时，可诊断为症状性便秘。",
//            "正常白带呈白色，时透明，时粘稠，无异味，而异常的白带主要是以下症状：\n" +
//                    "1、无色浆糊样白带像浆糊发粘，量多，常浸染于内裤。\n" +
//                    "2、豆渣样白带量多，状如豆渣呈絮状，并伴有阴道奇痒，。\n" +
//                    "3、泡沫样白带呈黄脓泡沫状，量多，伴有瘙痒，检验可看到活动的滴虫。\n" +
//                    "4、脓性白带呈黄色或绿色脓样，伴有周身无力、低热等症状，化脓是细菌感染。\n" +
//                    "5、水样白带清澈如水，常湿透内裤，会有一股臭味。",
//            //感冒
//            "鼻部症状有喷嚏、鼻塞、流清水样鼻涕等。也伴随有咳嗽、咽干、咽痒或灼热感。2～3天后鼻涕变稠，常伴咽痛、流泪、味觉减退、呼吸不畅、声嘶等。",
//            //孕吐
//            "孕吐是早孕反应的一种。妊娠以后，大约从第5周开始（也有更早些开始的）会发生孕吐。特别在早晚会出现恶心，没有任何原因就发生呕吐。到第16周，孕吐即可痊愈。",
//            //嗜睡
//            "嗜睡一般会出现在孕妈妈怀孕的早期出现，常常表现为困乏无比，精神不济，动不动就想睡觉，昼夜如此，生活作息会因此有所变化，但是，不必过于担心，这都是孕早期的正常生理现象。",
//            //浮肿
//            "怀孕期间，你可能会发现自己的脚踝和脚肿得厉害，这是由于组织中积聚了过多的液体造成的。此外，血液中的化学变化也会令一部分液体转移到身体的组织中，造成孕期浮肿。",
//            //尿频
//            "孕期尿频是每个孕妇都会遇到的问题，所谓的“尿频”是白天解尿次数超过7次，晚上解尿次数超过2次以上，且解尿间隔在2个小时内。特别是孕早期与晚期，易有尿频症状发生。",
//            //
//            "便秘一般可分为弛缓性、痉挛性、直肠性三种。孕妈妈在孕期中的便秘多为弛缓性便秘，它通常会在怀孕后第4个月开始出现，而到了妊娠晚期会越来越严重。",
//            "孕妇在孕期感到疲劳，特别是在孕早期感到疲劳是特别正常的。大多数孕妇对孕早期印象最深的就是，她们总是觉得很疲劳。怀孕使你全身紧张，所以你会感觉特别疲劳。",
//            "孕期，孕妈咪容易出现的皮肤易敏感衰老、色素沉淀、妊娠性皮肤瘙痒等问题，致使皮肤变差、皮肤异常现象。有的呈现阶段性，但是有的可能会伴随孕妈咪整个妊娠，直到宝宝出生。",
//            "当准妈妈血糖过低、发烧;胎儿缺氧、外界刺激、孕妈高血压、以及外界噪音的刺激;脐带绕颈等就会出现异常胎动。正常情况1小时不少于3～5次，12小时为30～40次。",
//            "在怀孕的中后期开始，孕妈的分泌物就会出现明显的变化。而且根据每个人的不同情况，其分泌物增多的程度也不同。正常来说，怀孕之后的孕妈分泌物都会增加，而且其中水分增多，粘性增大。"
//
//
//    };
//
//    /**
//     * 获取症状解释说明
//     *
//     * @param type
//     * @return
//     */
//    public static String getSymptomDefine(int type) {
//        try {
//            return SYMPTOMS_DEFINE[type];
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return "";
//
//    }
//
//}
//
//
//
//
//
//
//
//
//
//
//
//
